package kids.baba.mobile.presentation.viewmodel

import android.util.Log
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
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.view.FunctionHolder
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.model.EditGroupBottomSheetUiModel
import kids.baba.mobile.presentation.util.flow.asEventFlow
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
    val uiModel = MutableStateFlow(EditGroupBottomSheetUiModel())
    val groupName = MutableStateFlow(savedStateHandle["groupName"] ?: "")
    var itemClick = {}
    var dismiss: () -> Unit = {}

    private val _color = MutableStateFlow("")
    val color = _color.asStateFlow()

    private val nameViewState: MutableStateFlow<String> = MutableStateFlow("")

    private val isFamily = MutableStateFlow<Boolean?>(savedStateHandle["family"])

    private val _eventFlow = MutableEventFlow<EditGroupSheetEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    init {
        uiModel.value.permissionDesc = if (isFamily.value == true) "있음" else "없음"
    }

    val composableNameViewData = ComposableNameViewData(
        text = nameViewState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                when (patchOneGroupUseCase.patch(
                    group = GroupInfo(relationGroup = nameViewState.value), groupName = groupName.value
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(EditGroupSheetEvent.SuccessPatchGroupRelation)
                        itemClick()
                        dismiss()
                    }
                    is Result.NetworkError -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))
                        dismiss()
                    }
                    else -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
                        dismiss()
                    }
                }
            }
        }
    )

    val goToInviteMember = ComposableAddButtonViewData(
        onAddButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(EditGroupSheetEvent.GoToAddMemberPage)
            }
        }
    )

    val deleteGroup = ComposableDeleteViewData(
        onDeleteButtonClickEventListener = {
            viewModelScope.launch {
                when (deleteOneGroupUseCase.delete(
                    groupName = groupName.value
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(EditGroupSheetEvent.SuccessDeleteGroup)
                        Log.e("EditGroupBottomSheetViewModel", "delete success")
                        dismiss()
                    }
                    is Result.NetworkError -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.baba_network_failed))
                        dismiss()
                    }
                    else -> {
                        _eventFlow.emit(EditGroupSheetEvent.ShowSnackBar(R.string.unknown_error_msg))
                        dismiss()
                    }
                }
            }
        }
    )


    fun setColor(color: ColorUiModel) {
        _color.value = color.value
    }

}