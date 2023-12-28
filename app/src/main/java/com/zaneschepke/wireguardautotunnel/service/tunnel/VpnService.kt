package com.zaneschepke.wireguardautotunnel.service.tunnel

import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel
import com.wireguard.crypto.Key
import com.zaneschepke.wireguardautotunnel.data.model.TunnelConfig
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface VpnService : Tunnel {
    suspend fun startTunnel(tunnelConfig: TunnelConfig): Tunnel.State

    suspend fun stopTunnel()

    val vpnState: StateFlow<VpnState>

    fun getState(): Tunnel.State
}
