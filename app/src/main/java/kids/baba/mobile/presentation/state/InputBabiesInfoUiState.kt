package kids.baba.mobile.presentation.state

sealed class InputBabiesInfoUiState {
    object Loading : InputBabiesInfoUiState()
    data class CheckHaveInviteCode(val haveInviteCode: Boolean) : InputBabiesInfoUiState()
    object InputBabyName : InputBabiesInfoUiState()
    object InputBabyBirthDay : InputBabiesInfoUiState()
    object CheckMoreBaby : InputBabiesInfoUiState()
    object InputRelation : InputBabiesInfoUiState()
    data class ModifyName(val position: Int) : InputBabiesInfoUiState()
    data class ModifyHaveInviteCode(val position: Int) : InputBabiesInfoUiState()
    object InputInviteCode : InputBabiesInfoUiState()
    object CheckInviteCode : InputBabiesInfoUiState()
    object InputEnd : InputBabiesInfoUiState()
}
