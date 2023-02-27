package kids.baba.mobile.presentation.state

sealed class LoginUiState {
    object Loading : LoginUiState()
    object Success : LoginUiState()
    object LoginCanceled : LoginUiState()
    data class NeedToSignUp(val signToken: String) : LoginUiState()
    object Failure : LoginUiState()
}