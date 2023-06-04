package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.EditBabyNameUseCase
import kids.baba.mobile.presentation.binding.ComposableNameViewData
import kids.baba.mobile.presentation.event.BabyEditEvent
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyEditProfileBottomSheetViewModel @Inject constructor(
    private val editBabyNameUseCase: EditBabyNameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<BabyEditEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val name = MutableStateFlow("아이 이름")
    val button = MutableStateFlow("편집")

    val baby = MutableStateFlow<MemberUiModel?>(savedStateHandle[BABY_DETAIL_INFO])

    private val nameViewState: MutableStateFlow<String> = MutableStateFlow(baby.value?.name ?: "")

    val composableNameViewData = ComposableNameViewData(
        initialText = baby.value?.name ?: "",
        text = nameViewState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                when (editBabyNameUseCase.edit(
                    babyId = baby.value?.memberId ?: "",
                    name = nameViewState.value
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(BabyEditEvent.SuccessBabyEdit(babyName = nameViewState.value))
                    }
                    is Result.NetworkError -> {
                        _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.baba_network_failed))
                    }
                    else -> {
                        _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.unknown_error_msg))
                    }
                }
            }
        }
    )

}