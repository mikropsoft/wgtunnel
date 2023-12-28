package com.zaneschepke.wireguardautotunnel.ui.screens.config

import com.zaneschepke.wireguardautotunnel.Packages
import com.zaneschepke.wireguardautotunnel.data.model.TunnelConfig
import com.zaneschepke.wireguardautotunnel.ui.models.InterfaceProxy
import com.zaneschepke.wireguardautotunnel.ui.models.PeerProxy
import com.zaneschepke.wireguardautotunnel.util.Error

data class ConfigUiState(
    val proxyPeers: List<PeerProxy> = arrayListOf(PeerProxy()),
    val interfaceProxy: InterfaceProxy = InterfaceProxy(),
    val packages: Packages = emptyList(),
    val checkedPackageNames: List<String> = emptyList(),
    val include: Boolean = true,
    val isAllApplicationsEnabled : Boolean = false,
    val isLoading: Boolean = true,
    val tunnel: TunnelConfig? = null,
    val tunnelName: String = "",
    val errorEvent: Error = Error.NONE
)