package com.example.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Result
import com.example.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    data class SettingUiState(
        val result: Result<Int>? = null,
    )

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    init {
        settingRepository.fetchRequestLimit().onEach {
            _uiState.value = _uiState.value.copy(result = it)
        }.launchIn(viewModelScope)
    }

    fun onLimitSelected(limit: Int) {
        settingRepository.editRequestLimit(limit).onEach {
            _uiState.value = _uiState.value.copy(result = it)
        }.launchIn(viewModelScope)
    }
}
