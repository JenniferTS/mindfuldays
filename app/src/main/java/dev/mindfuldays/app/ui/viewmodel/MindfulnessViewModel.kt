package dev.mindfuldays.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mindfuldays.app.data.model.MindfulnessAttitude
import dev.mindfuldays.app.data.remote.GeminiApiService
import dev.mindfuldays.app.data.repository.AttitudeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class MindfulnessUiState(
    val attitude: MindfulnessAttitude,
    val aiReflection: String = "",
    val isLoadingAi: Boolean = false
)

class MindfulnessViewModel(
    private val repository: AttitudeRepository = AttitudeRepository(),
    private val geminiService: GeminiApiService = GeminiApiService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MindfulnessUiState(attitude = repository.getTodayAttitude())
    )
    val uiState: StateFlow<MindfulnessUiState> = _uiState.asStateFlow()

    val totalAttitudes: Int get() = repository.attitudes.size

    fun generateAiReflection(
        attitudeTitle: String? = null,
        languageCode: String = Locale.getDefault().language
    ) {
        if (_uiState.value.isLoadingAi) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingAi = true) }
            val title = attitudeTitle ?: _uiState.value.attitude.name
            val reflection = geminiService.generateReflection(title, languageCode)
            _uiState.update { it.copy(aiReflection = reflection, isLoadingAi = false) }
        }
    }
}
