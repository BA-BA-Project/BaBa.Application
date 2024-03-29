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
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyEditProfileBottomSheetViewModel @Inject constructor(
    private val editBabyNameUseCase: EditBabyNameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _eventFlow = MutableEventFlow<BabyEditEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val baby = MutableStateFlow<BabyUiModel?>(savedStateHandle[BABY_DETAIL_INFO])

    private val nameViewState: MutableStateFlow<String> = MutableStateFlow(baby.value?.name ?: "")

    private val _nameFocus = MutableStateFlow(false)
    val nameFocus = _nameFocus.asStateFlow()

    val composableNameViewData = ComposableNameViewData(
        initialText = baby.value?.name ?: "",
        text = nameViewState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                if (it) {
                    when (editBabyNameUseCase.edit(
                        babyId = baby.value?.babyId ?: "",
                        name = nameViewState.value
                    )) {
                        is Result.Success -> _eventFlow.emit(BabyEditEvent.SuccessBabyEdit(babyName = nameViewState.value))

                        is Result.NetworkError -> _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.baba_network_failed))

                        else -> _eventFlow.emit(BabyEditEvent.ShowSnackBar(R.string.already_have_same_name_or_format_error))
                    }
                } else {
                    _eventFlow.emit(BabyEditEvent.NameInput)
                }
            }
        },
        maxLength = 6,
    )

    fun changeNameFocus(hasFocus: Boolean) = _nameFocus.update { hasFocus }

}