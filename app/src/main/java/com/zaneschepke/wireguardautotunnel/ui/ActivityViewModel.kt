package com.zaneschepke.wireguardautotunnel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaneschepke.wireguardautotunnel.repository.SettingsDoa
import com.zaneschepke.wireguardautotunnel.repository.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val settingsRepo: SettingsDoa,
) : ViewModel() {
//    val settings = settingsRepo.getSettingsFlow().stateIn(viewModelScope,
//        SharingStarted.WhileSubscribed(5000L), Settings()
//    )
}
