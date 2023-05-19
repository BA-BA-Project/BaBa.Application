package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.MemberUiModel

sealed class MyPageEvent{
    object StartAddBabyPage: MyPageEvent()
    object StartInvitePage: MyPageEvent()
    data class StartBabyDetailPage(val data: MemberUiModel): MyPageEvent()
    object StartInviteMemberPage: MyPageEvent()
    object StartSettingPage: MyPageEvent()
    object StartAddGroupPage: MyPageEvent()

}
