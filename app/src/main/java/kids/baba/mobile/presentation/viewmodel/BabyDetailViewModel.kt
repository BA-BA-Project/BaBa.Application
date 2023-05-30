package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.DeleteOneBabyUseCase
import kids.baba.mobile.domain.usecase.GetBabyProfileUseCase
import kids.baba.mobile.presentation.model.BabyDetailUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.state.BabyDetailUiState
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
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
    val baby = MutableStateFlow<MemberUiModel?>(savedStateHandle[BABY_DETAIL_INFO])

    fun load(babyId: String) = viewModelScope.launch {
        getBabyProfileUseCase.get(babyId).catch {

        }.collect {
            _uiState.value = BabyDetailUiState.Success(it)
        }
    }

    fun delete(babyId: String) = viewModelScope.launch {
        deleteOneBabyUseCase.delete(babyId)
    }

    fun refresh(babyName: String) = viewModelScope.launch {
        uiModel.update {
            it.copy(
                babyName = babyName
            )
        }
    }
}