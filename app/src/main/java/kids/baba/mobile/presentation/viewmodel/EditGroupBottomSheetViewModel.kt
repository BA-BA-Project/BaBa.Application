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
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet.Companion.IS_FAMILY_KEY
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_COLOR
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupBottomSheetViewModel @Inject constructor(
    private val patchOneGroupUseCase: PatchOneGroupUseCase,
    private val deleteOneGroupUseCase: DeleteOneGroupUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 기존 그룹 네임과 기존 그룹 컬러
    val groupName = savedStateHandle.get<String>(GROUP_NAME) ?: ""
    val groupColor = savedStateHandle.get<String>(GROUP_COLOR) ?: ColorModel.PINK.colorCode

    // 바꿀 그룹 네임과 바꿀 그룹 컬러
    val nameViewState = MutableStateFlow(savedStateHandle[GROUP_NAME] ?: "")
    val groupColorState = MutableStateFlow(savedStateHandle[GROUP_COLOR] ?: ColorModel.PINK.colorCode)

    val isFamily = MutableStateFlow(savedStateHandle[IS_FAMILY_KEY] ?: false)

    private val _eventFlow = MutableEventFlow<EditGroupSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val composableNameViewData = ComposableNameViewData(
        initialText = groupName,
        enabled = false,
        text = nameViewState,
        maxLength = 6,
    )

    val goToInviteMember = ComposableAddButtonViewData(
        onAddButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(EditGroupSheetEvent.GoToAddMemberPage(nameViewState.value))
            }
        }
    )

    val deleteGroup = ComposableDeleteViewData(
        isFamily = isFamily.value,
        onDeleteButtonClickEventListener = {
            viewModelScope.launch {
                when (deleteOneGroupUseCase.delete(
                    groupName = groupName
                )) {
                    is Result.Success -> _eventFlow.emit(EditGroupSheetEvent.SuccessDeleteGroup)

                    is Result.NetworkError -> _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))

                    else -> _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
                }
            }
        }
    )

    fun editGroupInfo() = viewModelScope.launch {
        when (patchOneGroupUseCase.patch(
            group = GroupInfo(relationGroup = nameViewState.value, groupColor = groupColorState.value),
            groupName = groupName
        )) {
            is Result.Success -> _eventFlow.emit(EditGroupSheetEvent.SuccessPatchGroupRelation)

            is Result.NetworkError -> _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))

            else -> _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
        }
    }


    fun setColor(color: ColorUiModel) {
        groupColorState.value = color.value
    }

}