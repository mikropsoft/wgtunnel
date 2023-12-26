package com.zaneschepke.wireguardautotunnel.ui.screens.settings

import com.wireguard.android.backend.Tunnel
import com.zaneschepke.wireguardautotunnel.data.model.Settings
import com.zaneschepke.wireguardautotunnel.data.model.TunnelConfig

data class SettingsUiState(
    val settings : Settings = Settings(),
    val tunnels : List<TunnelConfig> = emptyList(),
    val tunnelState : Tunnel.State = Tunnel.State.DOWN,
    val isLocationDisclosureShown : Boolean = true,
    val loading : Boolean = true,
)
