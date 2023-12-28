package com.zaneschepke.wireguardautotunnel.ui.screens.config

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ConfigViewModelFactory {
    fun create(configId: String): ConfigViewModel
}