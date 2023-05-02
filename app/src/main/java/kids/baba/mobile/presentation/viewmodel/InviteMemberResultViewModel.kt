package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InviteMemberResultViewModel @Inject constructor() : ViewModel() {
    val tvTopTitleText = MutableStateFlow("멤버 초대")
    val tvBannerTitleText = MutableStateFlow("멤버 초대를 완료했어요!")
    val tvBannerDescText = MutableStateFlow("멤버들과 소통하며 즐거운 시간 보내요 :)")
    val tvGroupTitleText = MutableStateFlow("초대 멤버의 소속 그룹")
    val tvGroupDescText = MutableStateFlow("친구")
    val tvRelationTitleText = MutableStateFlow("초대 멤버와 아이의 관계")
    val tvRelationDescText = MutableStateFlow("이모")
    val tvPermissionTitleText = MutableStateFlow("성장앨범 업로드 권한")
    val tvPermissionDescText = MutableStateFlow("없음")
}