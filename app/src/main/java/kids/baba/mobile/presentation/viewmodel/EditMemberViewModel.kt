package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupMemberUseCase
import kids.baba.mobile.domain.usecase.PatchOneMemberUseCase
import kids.baba.mobile.presentation.model.MemberUiModel
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
    val nameTitle = MutableStateFlow("이름")
    val name = MutableStateFlow("이재임")
    val deleteTitle = MutableStateFlow("멤버 삭제하기")
    val delete = MutableStateFlow("삭제")
    val member = MutableStateFlow<MemberUiModel?>(savedStateHandle[EditMemberDialog.SELECTED_MEMBER_KEY])
    val relation = MutableStateFlow<String?>(savedStateHandle[EditMemberDialog.SELECTED_MEMBER_RELATION])
    fun patch(relation: String) = viewModelScope.launch {
        patchOneMemberUseCase.patch(
            memberId = member.value?.memberId ?: "",
            relation = GroupMemberInfo(relationName = relation)
        )
    }

    fun delete() = viewModelScope.launch {
        deleteOneGroupMemberUseCase.delete(member.value?.memberId ?: "")
    }
}