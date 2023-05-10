package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupUseCase
import kids.baba.mobile.domain.usecase.PatchOneGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupBottomSheetViewModel @Inject constructor(
    private val patchOneGroupUseCase: PatchOneGroupUseCase,
    private val deleteOneGroupUseCase: DeleteOneGroupUseCase
) : ViewModel() {
    val familyName = MutableStateFlow("가족 이름")
    val familyButton = MutableStateFlow("편집")
    val permission = MutableStateFlow("성장앨범 업로드 권한")
    val groupColor = MutableStateFlow("그룹 컬러")
    val permissionDesc = MutableStateFlow("없음")
    val deleteTitle = MutableStateFlow("그룹 삭제")
    val deleteDesc = MutableStateFlow("삭제하기")
    val addMemberTitle = MutableStateFlow("멤버 초대")
    val query = MutableStateFlow("")

    fun patch(name: String) = viewModelScope.launch {
        patchOneGroupUseCase.patch(group = GroupInfo(relationGroup = name), groupName = query.value)
    }

    fun delete() = viewModelScope.launch {
        deleteOneGroupUseCase.delete(groupName = query.value)
    }
}