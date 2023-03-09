package kids.baba.mobile.presentation.state

sealed class InputChildrenInfoUiState {
    object Loading : InputChildrenInfoUiState()
    data class CheckHaveInviteCode(val haveInviteCode: Boolean) : InputChildrenInfoUiState()
    object InputBabyName : InputChildrenInfoUiState()
    object InputBabyBirthDay : InputChildrenInfoUiState()
    object CheckMoreBaby : InputChildrenInfoUiState()
    object InputRelation : InputChildrenInfoUiState()
    data class ModifyText(val position: Int) : InputChildrenInfoUiState()
    data class ModifySelection(val position: Int) : InputChildrenInfoUiState()
    object InputInviteCode : InputChildrenInfoUiState()
    object CheckInviteCode : InputChildrenInfoUiState()
    object InputEnd : InputChildrenInfoUiState()
}
