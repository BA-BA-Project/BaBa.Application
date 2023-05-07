package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.Group

sealed class MyPageUiState {
    object Idle: MyPageUiState()

    object Loading : MyPageUiState()

    data class LoadMember(val data: List<Group>) : MyPageUiState()

    data class Error(val t: Throwable) : MyPageUiState()
}
