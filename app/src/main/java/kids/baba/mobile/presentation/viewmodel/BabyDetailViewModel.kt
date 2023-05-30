package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.DeleteOneBabyUseCase
import kids.baba.mobile.domain.usecase.GetBabyProfileUseCase
import kids.baba.mobile.presentation.event.BabyDetailEvent
import kids.baba.mobile.presentation.model.BabyDetailUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.state.BabyDetailUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _eventFlow = MutableEventFlow<BabyDetailEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val baby = MutableStateFlow<MemberUiModel?>(savedStateHandle[BABY_DETAIL_INFO])

    init {
        uiModel.value.babyName = baby.value?.name ?: ""
        load(baby.value?.memberId ?: "")
    }

    private fun load(babyId: String) = viewModelScope.launch {

        when (val result = getBabyProfileUseCase.get(babyId)) {
            is Result.Success -> {
                Log.e("BabyDetailViewModel", "load: ${result.data}")
                _eventFlow.emit(BabyDetailEvent.SuccessBabyDetail(result.data))
                uiModel.update {
                    it.copy(
                        babyBirthday = result.data.birthday,
                        familyGroupTitle = result.data.familyGroup.groupName,
                        myGroupTitle =
                        if (result.data.myGroup != null) {
                            result.data.myGroup.groupName
                        } else {
                            "내가 속한 그룹 이름"
                        }
                    )
                }
            }
            is Result.NetworkError -> {
                _eventFlow.emit(BabyDetailEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> _eventFlow.emit(BabyDetailEvent.ShowSnackBar(R.string.unknown_error_msg))
        }
    }


    fun onBackClick() = viewModelScope.launch {
        _eventFlow.emit(BabyDetailEvent.BackButtonClicked)
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