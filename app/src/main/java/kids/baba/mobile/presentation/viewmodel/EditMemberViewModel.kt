package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupMemberUseCase
import kids.baba.mobile.domain.usecase.PatchOneMemberUseCase
import kids.baba.mobile.presentation.model.EditMemberUiModel
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val patchOneMemberUseCase: PatchOneMemberUseCase,
    private val deleteOneGroupMemberUseCase: DeleteOneGroupMemberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(EditMemberUiModel())
    val input = MutableStateFlow("")
    val dismiss = MutableStateFlow {}
    val patchMember = MutableStateFlow {}
    init {
        uiModel.value.member = savedStateHandle[EditMemberDialog.SELECTED_MEMBER_KEY]
        uiModel.value.relation = savedStateHandle[EditMemberDialog.SELECTED_MEMBER_RELATION]
    }
    fun patch() = viewModelScope.launch {
        patchOneMemberUseCase.patch(
            memberId = uiModel.value.member?.memberId ?: "",
            relation = GroupMemberInfo(relationName = input.value)
        )
        patchMember.value()
        dismiss()
    }

    fun delete() = viewModelScope.launch {
        deleteOneGroupMemberUseCase.delete(uiModel.value.member?.memberId ?: "")
        Log.e("123","delete")
        dismiss()
    }
    fun dismiss() = viewModelScope.launch {
        dismiss.value()
    }
}