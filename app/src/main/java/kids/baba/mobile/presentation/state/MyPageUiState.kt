package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.model.MemberUiModel

sealed class MyPageUiState {
    object Idle : MyPageUiState()

    object Loading : MyPageUiState()

    data class LoadMember(val data: List<Group>) : MyPageUiState()

    data class LoadBabies(val data: List<Baby>) : MyPageUiState()

    data class LoadMyInfo(val data: MemberUiModel) : MyPageUiState()

    data class Error(val t: Throwable) : MyPageUiState()
}
