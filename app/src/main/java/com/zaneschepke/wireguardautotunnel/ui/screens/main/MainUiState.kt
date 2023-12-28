package com.zaneschepke.wireguardautotunnel.ui.screens.main

import com.zaneschepke.wireguardautotunnel.TunnelConfigs
import com.zaneschepke.wireguardautotunnel.data.model.Settings
import com.zaneschepke.wireguardautotunnel.service.tunnel.VpnState
import com.zaneschepke.wireguardautotunnel.util.Error

data class MainUiState(
    val settings : Settings = Settings(),
    val tunnels : TunnelConfigs = emptyList(),
    val vpnState: VpnState = VpnState(),
    val loading : Boolean = true,
    val errorEvent : Error = Error.NONE
)
