package com.romanpolach.peacefulflight.kmp.viewmodel

import androidx.lifecycle.ViewModel
import com.romanpolach.peacefulflight.kmp.utils.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

data class RidingTheWaveUiState(
    val currentStepIndex: Int = 0,
    val currentTextRes: StringResource? = null,
    val isLastStep: Boolean = false,
    val anxietyScore: Float = 5f,
    val feedbackMessageRes: StringResource? = null,
    val showSuccessDialog: Boolean = false,
    val isAutoPlayEnabled: Boolean = false
)

class RidingTheWaveViewModel(
    private val ttsManager: TtsManager
) : ViewModel() {

    private var steps: List<StringResource> = emptyList()
    private var isInitialized = false

    private val _uiState = MutableStateFlow(RidingTheWaveUiState())
    val uiState: StateFlow<RidingTheWaveUiState> = _uiState.asStateFlow()

    private val anxietyHistory = mutableListOf<Int>()

    fun initialize(stepResources: List<StringResource>) {
        if (!isInitialized && stepResources.isNotEmpty()) {
            steps = stepResources
            _uiState.update {
                it.copy(
                    currentStepIndex = 0,
                    currentTextRes = stepResources[0],
                    isLastStep = stepResources.size == 1
                )
            }
            isInitialized = true
        }
    }

    fun toggleTts(text: String) {
        if (_uiState.value.isAutoPlayEnabled) {
            ttsManager.stop()
            _uiState.update { it.copy(isAutoPlayEnabled = false) }
        } else {
            ttsManager.speak(text)
            _uiState.update { it.copy(isAutoPlayEnabled = true) }
        }
    }

    fun onStepContentChanged(text: String) {
        if (_uiState.value.isAutoPlayEnabled) {
            ttsManager.speak(text)
        }
    }

    fun nextStep() {
        ttsManager.stop()
        val currentIndex = _uiState.value.currentStepIndex
        if (currentIndex < steps.size - 1) {
            val newIndex = currentIndex + 1
            _uiState.update {
                it.copy(
                    currentStepIndex = newIndex,
                    currentTextRes = steps[newIndex],
                    isLastStep = newIndex == steps.size - 1
                )
            }
        }
    }

    fun updateAnxietyScore(score: Float) {
        _uiState.update { it.copy(anxietyScore = score) }
    }

    fun submitRating(
        feedbackImproving: StringResource,
        feedbackWorsening: StringResource,
        feedbackSteady: StringResource
    ) {
        val currentScore = _uiState.value.anxietyScore.toInt()
        anxietyHistory.add(currentScore)

        val firstScore = anxietyHistory.firstOrNull() ?: currentScore
        val feedback = when {
            currentScore < firstScore -> feedbackImproving
            currentScore > firstScore -> feedbackWorsening
            else -> feedbackSteady
        }
        _uiState.update { it.copy(feedbackMessageRes = feedback) }
    }

    fun finishSession(onFinish: () -> Unit) {
        val startScore = anxietyHistory.firstOrNull()
        val endScore = anxietyHistory.lastOrNull()

        if (startScore != null && endScore != null && (startScore - endScore >= 2)) {
            _uiState.update { it.copy(showSuccessDialog = true) }
        } else {
            onFinish()
        }
    }

    fun closeDialog(onFinish: () -> Unit) {
        _uiState.update { it.copy(showSuccessDialog = false) }
        onFinish()
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.stop()
    }
}
