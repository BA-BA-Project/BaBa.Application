package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.DeleteOneBabyUseCase
import kids.baba.mobile.domain.usecase.GetBabyProfileUseCase
import kids.baba.mobile.presentation.model.BabyDetailUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.state.BabyDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyDetailViewModel @Inject constructor(
    private val getBabyProfileUseCase: GetBabyProfileUseCase,
    private val deleteOneBabyUseCase: DeleteOneBabyUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(BabyDetailUiModel())

    private val _uiState = MutableStateFlow<BabyDetailUiState>(BabyDetailUiState.Idle)
    val uiState = _uiState.asStateFlow()
    val baby = MutableStateFlow<MemberUiModel?>(savedStateHandle["baby"])

    fun load(babyId: String) = viewModelScope.launch {
        getBabyProfileUseCase.get(babyId).catch {

        }.collect {
            _uiState.value = BabyDetailUiState.Success(it)
        }
    }

    fun delete(babyId: String) = viewModelScope.launch {
        deleteOneBabyUseCase.delete(babyId)
    }
}