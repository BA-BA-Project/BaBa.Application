package kids.baba.mobile.presentation.state

sealed class SignUpUiState{
    object Loading: SignUpUiState()
    object SelectGreeting: SignUpUiState()
    object InputName: SignUpUiState()
    object ModifyName: SignUpUiState()
    object SelectProfile: SignUpUiState()
}
