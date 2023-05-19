package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.MemberUiModel

sealed class MyPageEvent{
    object MoveToAddBabyPage: MyPageEvent()
    object MoveToInvitePage: MyPageEvent()
    data class MoveToBabyDetailPage(val data: MemberUiModel): MyPageEvent()
    object MoveToInviteMemberPage: MyPageEvent()
    object MoveToSettingPage: MyPageEvent()
    object MoveToAddGroupPage: MyPageEvent()

}
