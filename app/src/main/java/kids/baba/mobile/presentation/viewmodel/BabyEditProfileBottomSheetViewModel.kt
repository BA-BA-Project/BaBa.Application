package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
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
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditProfileBottomSheet
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

    val baby = MutableStateFlow<MemberUiModel?>(savedStateHandle[BabyEditProfileBottomSheet.SELECTED_BABY_KEY])

    private val nameViewLiveData: MutableLiveData<String> = MutableLiveData("")

    val composableNameViewData = ComposableNameViewData(
        text = nameViewLiveData,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                when (editBabyNameUseCase.edit(
                    babyId = baby.value?.memberId ?: "",
                    name = nameViewLiveData.value ?: ""
                )) {
                    is Result.Success -> _eventFlow.emit(BabyEditEvent.SuccessBabyEdit)
                    is Result.NetworkError -> _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.baba_network_failed))
                    else -> _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.unknown_error_msg))
                }
            }
        }
    )

}