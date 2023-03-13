package kids.baba.mobile.presentation.state

sealed class CreateProfileUiState{
    object Loading: CreateProfileUiState()
    object SelectGreeting: CreateProfileUiState()
    object InputName: CreateProfileUiState()
    data class ModifyName(val position: Int): CreateProfileUiState()
    object SelectProfileIcon: CreateProfileUiState()
    object EndCreateProfile: CreateProfileUiState()
}
