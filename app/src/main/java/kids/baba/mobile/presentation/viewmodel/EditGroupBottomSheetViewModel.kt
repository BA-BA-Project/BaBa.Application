package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditGroupBottomSheetViewModel @Inject constructor() : ViewModel() {
    val familyName = MutableStateFlow("가족 이름")
    val familyButton = MutableStateFlow("편집")
    val permission = MutableStateFlow("성장앨범 업로드 권한")
    val groupColor = MutableStateFlow("그룹 컬러")
    val permissionDesc = MutableStateFlow("없음")
    val deleteTitle = MutableStateFlow("그룹 삭제")
    val deleteDesc = MutableStateFlow("삭제하기")
    val addMemberTitle = MutableStateFlow("멤버 초대")
}