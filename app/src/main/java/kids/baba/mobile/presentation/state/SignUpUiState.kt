package kids.baba.mobile.presentation.state

sealed class SignUpUiState{
    object Loading: SignUpUiState()
    object SelectGreeting: SignUpUiState()
    object InputName: SignUpUiState()
    data class ModifyName(val position: Int): SignUpUiState()
    object SelectProfile: SignUpUiState()
}
