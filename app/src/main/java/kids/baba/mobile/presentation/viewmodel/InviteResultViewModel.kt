package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InviteResultViewModel @Inject constructor():ViewModel() {
    val addBabyTitle = MutableStateFlow("아이 추가하기")
    val addBabyBannerTitle = MutableStateFlow("아이가 추가되었어요!")
    val addBabyBannerDesc = MutableStateFlow("아이의 소식을 받고 소통해봐요 :)")
    val addBabyNameTitle = MutableStateFlow("아이 이름")
    val addBabyNameDesc = MutableStateFlow("티티")
    val addBabyGroupTitle = MutableStateFlow("나의 소속 그룹")
    val addBabyGroupDesc = MutableStateFlow("친구")
    val addBabyRelationTitle = MutableStateFlow("나와 아이와 관계")
    val addBabyRelationDesc = MutableStateFlow("이모")
    val addBabyPermissionTitle = MutableStateFlow("성장앨범 업로드 권한")
    val addBabyPermissionDesc = MutableStateFlow("없음")
}