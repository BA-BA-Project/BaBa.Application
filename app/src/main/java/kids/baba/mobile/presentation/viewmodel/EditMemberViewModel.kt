package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.usecase.DeleteOneGroupMemberUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.PatchOneMemberRelationUseCase
import kids.baba.mobile.presentation.binding.ComposableDeleteViewData
import kids.baba.mobile.presentation.event.EditGroupMemberEvent
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val patchOneMemberRelationUseCase: PatchOneMemberRelationUseCase,
    private val deleteOneGroupMemberUseCase: DeleteOneGroupMemberUseCase,
    private val getMemberUseCase: GetMemberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val member = savedStateHandle.get<MemberUiModel>(EditMemberDialog.SELECTED_MEMBER_KEY)
    val groupName = savedStateHandle.get<String>(EditMemberDialog.SELECTED_GROUP_KEY)

    val relationWithBaby = MutableStateFlow(member?.introduction ?: "")

    private val _eventFlow = MutableEventFlow<EditGroupMemberEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _relationFocus = MutableStateFlow(false)
    val relationFocus = _relationFocus.asStateFlow()


    fun inputHandling(isComplete: Boolean) {
        if (isComplete) {
            patch()
        } else {
            viewModelScope.launch {
                _eventFlow.emit(EditGroupMemberEvent.RelationInput)
            }
        }
    }

    fun patch() = viewModelScope.launch {
        when (patchOneMemberRelationUseCase.patch(
            memberId = member?.memberId ?: "",
            relation = GroupMemberInfo(relationName = relationWithBaby.value)
        )) {
            is ApiResult.Success -> _eventFlow.emit(EditGroupMemberEvent.SuccessPatchMemberRelation)

            is ApiResult.NetworkError -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.baba_network_failed))

            else -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.invalid_format_error))

        }
    }

    val deleteMember = ComposableDeleteViewData(
        onDeleteButtonClickEventListener = {
            viewModelScope.launch {
                when (val myInfo = getMemberUseCase.getMe()) {
                    is ApiResult.Success -> {
                        if (member?.memberId == myInfo.data.memberId) {
                            _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.cannot_delete_myself))
                            return@launch
                        }
                        when (deleteOneGroupMemberUseCase.delete(memberId = member?.memberId ?: "")) {
                            is ApiResult.Success -> _eventFlow.emit(EditGroupMemberEvent.SuccessDeleteMember)
                            is ApiResult.NetworkError -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.baba_network_failed))
                            else -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.invalid_delete_member_error))
                        }
                    }

                    else -> _eventFlow.emit(EditGroupMemberEvent.ShowSnackBar(R.string.load_my_info_error_message))
                }
            }
        }
    )

    fun dismiss() = viewModelScope.launch {
        _eventFlow.emit(EditGroupMemberEvent.DismissDialog)
    }

    fun relationChange(hasFocus: Boolean) = _relationFocus.update { hasFocus }

}