package kids.baba.mobile.presentation.state

sealed class LoginUiState {
    object Loading : LoginUiState()
    object Success : LoginUiState()
    object LoginCanceled : LoginUiState()
    object NeedToSignUp : LoginUiState()
    object Failure : LoginUiState()
}