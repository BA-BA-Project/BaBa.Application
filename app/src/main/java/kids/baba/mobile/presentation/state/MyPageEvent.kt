package kids.baba.mobile.presentation.state

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.model.MemberUiModel

sealed class MyPageEvent {
    object Idle : MyPageEvent()

    object Loading : MyPageEvent()

    data class LoadMember(val data: List<Group>) : MyPageEvent()

    data class LoadBabies(val data: List<Baby>) : MyPageEvent()

    data class LoadMyInfo(val data: MemberUiModel) : MyPageEvent()

    data class LoadGroup(val data: String) : MyPageEvent()

    data class Error(val t: Throwable) : MyPageEvent()

    data class ShowSnackBar(@StringRes val message: Int) : MyPageEvent()

    object AddGroup : MyPageEvent()

    object Setting : MyPageEvent()
}
