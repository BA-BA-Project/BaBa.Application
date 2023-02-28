package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.MemberModel

sealed class LoginUiState {
    object Loading : LoginUiState()
    data class Success(val member: MemberModel) : LoginUiState()
    object LoginCanceled : LoginUiState()
    data class NeedToSignUp(val signToken: String) : LoginUiState()
    object Failure : LoginUiState()
}