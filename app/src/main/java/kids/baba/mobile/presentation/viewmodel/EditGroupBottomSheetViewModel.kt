package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.DeleteOneGroupUseCase
import kids.baba.mobile.domain.usecase.PatchOneGroupUseCase
import kids.baba.mobile.presentation.binding.ComposableAddButtonViewData
import kids.baba.mobile.presentation.binding.ComposableDeleteViewData
import kids.baba.mobile.presentation.binding.ComposableNameViewData
import kids.baba.mobile.presentation.event.EditGroupSheetEvent
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet.Companion.IS_FAMILY_KEY
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupBottomSheetViewModel @Inject constructor(
    private val patchOneGroupUseCase: PatchOneGroupUseCase,
    private val deleteOneGroupUseCase: DeleteOneGroupUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupName = MutableStateFlow(savedStateHandle[GROUP_NAME] ?: "")
    val isFamily = MutableStateFlow(savedStateHandle[IS_FAMILY_KEY] ?: false)

    // TODO: 기존 컬러값을 가져올 수 있는가?
    private val _color = MutableStateFlow("#81E0D5")
    val color = _color.asStateFlow()

    private val nameViewState: MutableStateFlow<String> = MutableStateFlow(savedStateHandle[GROUP_NAME] ?: "")

    private val _eventFlow = MutableEventFlow<EditGroupSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val composableNameViewData = ComposableNameViewData(
        initialText = groupName.value,
        text = nameViewState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                when (patchOneGroupUseCase.patch(
                    group = GroupInfo(relationGroup = nameViewState.value),
                    groupName = groupName.value
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(EditGroupSheetEvent.SuccessPatchGroupRelation)
                    }

                    is Result.NetworkError -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))
                    }

                    else -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
                    }
                }
            }
        },
        maxLength = 6,
    )

    val goToInviteMember = ComposableAddButtonViewData(
        onAddButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(EditGroupSheetEvent.GoToAddMemberPage(groupName.value))
            }
        }
    )

    val deleteGroup = ComposableDeleteViewData(
        isFamily = isFamily.value,
        onDeleteButtonClickEventListener = {
            viewModelScope.launch {
                when (deleteOneGroupUseCase.delete(
                    groupName = groupName.value
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(EditGroupSheetEvent.SuccessDeleteGroup)
                    }

                    is Result.NetworkError -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))
                    }

                    else -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
                    }
                }
            }
        }
    )


    fun setColor(color: ColorUiModel) {
        _color.value = color.value
    }

}