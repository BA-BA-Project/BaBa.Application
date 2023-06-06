package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.DeleteOneGroupMemberUseCase
import kids.baba.mobile.domain.usecase.PatchOneMemberRelationUseCase
import kids.baba.mobile.presentation.binding.ComposableDeleteViewData
import kids.baba.mobile.presentation.event.EditGroupMemberEvent
import kids.baba.mobile.presentation.model.EditMemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val patchOneMemberRelationUseCase: PatchOneMemberRelationUseCase,
    private val deleteOneGroupMemberUseCase: DeleteOneGroupMemberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(EditMemberUiModel())
    val relationWithBaby = MutableStateFlow("")

    private val _eventFlow = MutableEventFlow<EditGroupMemberEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        uiModel.value.member = savedStateHandle[EditMemberDialog.SELECTED_MEMBER_KEY]
        uiModel.value.relation = savedStateHandle[EditMemberDialog.SELECTED_MEMBER_RELATION]
    }

    fun patch() = viewModelScope.launch {
        when (patchOneMemberRelationUseCase.patch(
            memberId = uiModel.value.member?.memberId ?: "",
            relation = GroupMemberInfo(relationName = relationWithBaby.value)
        )) {
            is Result.Success -> {
                _eventFlow.emit(EditGroupMemberEvent.SuccessPatchMemberRelation)
            }
            is Result.NetworkError -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> {
                _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }

    val deleteMember = ComposableDeleteViewData(
        onDeleteButtonClickEventListener = {
            viewModelScope.launch {
                when (deleteOneGroupMemberUseCase.delete(uiModel.value.member?.memberId ?: "")) {
                    is Result.Success -> {
                        _eventFlow.emit(EditGroupMemberEvent.SuccessDeleteMember)
                    }
                    is Result.NetworkError -> _eventFlow.emit(
                        EditGroupMemberEvent.ShowSnackBar(R.string.baba_network_failed)
                    )
                    else -> {
                        _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.unknown_error_msg))
                    }
                }
            }
        }
    )

    fun dismiss() = viewModelScope.launch {
        _eventFlow.emit(EditGroupMemberEvent.DismissDialog)
    }

}