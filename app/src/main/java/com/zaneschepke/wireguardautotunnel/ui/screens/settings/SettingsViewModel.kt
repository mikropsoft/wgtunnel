package com.zaneschepke.wireguardautotunnel.ui.screens.settings

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wireguard.android.backend.Tunnel
import com.wireguard.android.util.RootShell
import com.zaneschepke.wireguardautotunnel.repository.SettingsDoa
import com.zaneschepke.wireguardautotunnel.repository.TunnelConfigDao
import com.zaneschepke.wireguardautotunnel.repository.datastore.DataStoreManager
import com.zaneschepke.wireguardautotunnel.repository.model.Settings
import com.zaneschepke.wireguardautotunnel.service.foreground.ServiceManager
import com.zaneschepke.wireguardautotunnel.service.tunnel.VpnService
import com.zaneschepke.wireguardautotunnel.util.WgTunnelException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val application: Application,
    private val tunnelRepo: TunnelConfigDao,
    private val settingsRepo: SettingsDoa,
    private val dataStoreManager: DataStoreManager,
    private val rootShell: RootShell,
    private val vpnService: VpnService
) : ViewModel() {

    val settings = settingsRepo.getSettingsFlow().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000L), Settings())
    val tunnels = tunnelRepo.getAllFlow().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000L), emptyList())
    val vpnState get() = vpnService.state.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000L), Tunnel.State.DOWN)

    suspend fun onSaveTrustedSSID(ssid: String) {
        val trimmed = ssid.trim()
        if (!settings.value.trustedNetworkSSIDs.contains(trimmed)) {
            settings.value.trustedNetworkSSIDs.add(trimmed)
            settingsRepo.save(settings.value)
        } else {
            throw WgTunnelException("SSID already exists.")
        }
    }

    suspend fun isLocationDisclosureShown() : Boolean {
        return dataStoreManager.getFromStore(DataStoreManager.LOCATION_DISCLOSURE_SHOWN) ?: false
    }

    fun setLocationDisclosureShown() {
        viewModelScope.launch {
            dataStoreManager.saveToDataStore(DataStoreManager.LOCATION_DISCLOSURE_SHOWN, true)
        }
    }

    suspend fun onToggleTunnelOnMobileData() {
        settingsRepo.save(
            settings.value.copy(
                isTunnelOnMobileDataEnabled = !settings.value.isTunnelOnMobileDataEnabled
            )
        )
    }

    suspend fun onDeleteTrustedSSID(ssid: String) {
        settings.value.trustedNetworkSSIDs.remove(ssid)
        settingsRepo.save(settings.value)
    }

    private suspend fun getDefaultTunnelOrFirst() : String {
        return settings.value.defaultTunnel ?: tunnelRepo.getAll().first().wgQuick
    }

    suspend fun toggleAutoTunnel() {
        val defaultTunnel = getDefaultTunnelOrFirst()
        if (settings.value.isAutoTunnelEnabled) {
            ServiceManager.stopWatcherService(application)
        } else {
            ServiceManager.startWatcherService(application, defaultTunnel)
        }
        saveSettings(
            settings.value.copy(
                isAutoTunnelEnabled = settings.value.isAutoTunnelEnabled,
                defaultTunnel = defaultTunnel
            )
        )
    }

    suspend fun onToggleAlwaysOnVPN() {
        val updatedSettings =
            settings.value.copy(
                isAlwaysOnVpnEnabled = !settings.value.isAlwaysOnVpnEnabled,
                defaultTunnel = getDefaultTunnelOrFirst()
            )
        saveSettings(updatedSettings)
    }

    private suspend fun saveSettings(settings: Settings) {
        settingsRepo.save(settings)
    }

    suspend fun onToggleTunnelOnEthernet() {
        saveSettings(settings.value.copy(
            isTunnelOnEthernetEnabled = !settings.value.isTunnelOnEthernetEnabled
        ))
    }

    private fun isLocationServicesEnabled(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun isLocationServicesNeeded(): Boolean {
        return (!isLocationServicesEnabled() && Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
    }

    suspend fun onToggleShortcutsEnabled() {
        saveSettings(
            settings.value.copy(
                isShortcutsEnabled = !settings.value.isShortcutsEnabled
            )
        )
    }

    suspend fun onToggleBatterySaver() {
        saveSettings(
            settings.value.copy(
                isBatterySaverEnabled = !settings.value.isBatterySaverEnabled
            )
        )
    }

    private suspend fun saveKernelMode(on: Boolean) {
        saveSettings(
            settings.value.copy(
                isKernelEnabled = on
            )
        )
    }

    suspend fun onToggleTunnelOnWifi() {
        saveSettings(
            settings.value.copy(
                isTunnelOnWifiEnabled = !settings.value.isTunnelOnWifiEnabled
            )
        )
    }

    suspend fun onToggleKernelMode() {
        if (!settings.value.isKernelEnabled) {
            try {
                rootShell.start()
                Timber.d("Root shell accepted!")
                saveKernelMode(on = true)
            } catch (e: RootShell.RootShellException) {
                saveKernelMode(on = false)
                throw WgTunnelException("Root shell denied!")
            }
        } else {
            saveKernelMode(on = false)
        }
    }
}
