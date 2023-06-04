package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.presentation.model.GroupMember

sealed class BabyDetailEvent {
    data class SuccessBabyDetail(val familyMemberList: List<GroupMember>, val myGroupMemberList: List<GroupMember>) : BabyDetailEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyDetailEvent()
    object BackButtonClicked : BabyDetailEvent()
}
