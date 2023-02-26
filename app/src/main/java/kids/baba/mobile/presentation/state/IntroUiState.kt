package kids.baba.mobile.presentation.state

sealed class IntroUiState {
    object Loading : IntroUiState()
    object AlreadyLoggedIn : IntroUiState()
    object NeedToLogin : IntroUiState()
    object NeedToOnboard : IntroUiState()
}