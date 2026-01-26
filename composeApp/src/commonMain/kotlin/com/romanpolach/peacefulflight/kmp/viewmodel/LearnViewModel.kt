package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import com.romanpolach.peacefulflight.kmp.data.AppContent
import com.romanpolach.peacefulflight.kmp.model.LearnSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * UI State for the Learn screen.
 */
data class LearnUiState(
    val sections: List<LearnSection> = emptyList(),
    val expandedSectionId: String? = null
)

/**
 * ViewModel for the Learn screen.
 * Migrated from the original Android app.
 */
class LearnViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LearnUiState(sections = AppContent.learnSections))
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    fun toggleSection(sectionId: String) {
        _uiState.update {
            it.copy(expandedSectionId = if (it.expandedSectionId == sectionId) null else sectionId)
        }
    }
}
