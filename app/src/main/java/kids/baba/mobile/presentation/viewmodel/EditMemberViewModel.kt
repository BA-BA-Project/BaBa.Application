package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupMemberUseCase
import kids.baba.mobile.domain.usecase.PatchOneMemberUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(
    private val patchOneMemberUseCase: PatchOneMemberUseCase,
    private val deleteOneGroupMemberUseCase: DeleteOneGroupMemberUseCase
) : ViewModel() {
    val nameTitle = MutableStateFlow("이름")
    val name = MutableStateFlow("이재임")
    val deleteTitle = MutableStateFlow("멤버 삭제하기")
    val delete = MutableStateFlow("삭제")

    fun patch(memberId: String, relation: String) = viewModelScope.launch {
        patchOneMemberUseCase.patch(
            memberId = memberId,
            relation = GroupMemberInfo(relationName = relation)
        )
    }

    fun delete(memberId: String) = viewModelScope.launch {
        deleteOneGroupMemberUseCase.delete(memberId)
    }
}