package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.Member

sealed class MyPageUiState {
    object Idle: MyPageUiState()

    object Loading : MyPageUiState()

    data class LoadMember(val data: List<Member>) : MyPageUiState()

    data class Error(val t: Throwable) : MyPageUiState()
}
