package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for the SOS screen.
 */
data class SosUiState(
    val isActive: Boolean = false,
    val breathingTextKey: String = "breathe_in"
)

/**
 * ViewModel for the SOS screen.
 * Migrated from the original Android app.
 */
class SosViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SosUiState())
    val uiState: StateFlow<SosUiState> = _uiState.asStateFlow()
}
