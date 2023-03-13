package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.presentation.model.BabyInfo

sealed class InputBabiesInfoUiState {
    object Loading : InputBabiesInfoUiState()
    data class SignUpSuccess(val member: MemberModel): InputBabiesInfoUiState()
    data class SignUpFailed(val throwable: Throwable): InputBabiesInfoUiState()
    data class InputBabyName(val babyInfo: BabyInfo) : InputBabiesInfoUiState()
    data class InputBabyBirthDay(val babyInfo: BabyInfo) : InputBabiesInfoUiState()
    object CheckMoreBaby : InputBabiesInfoUiState()
    object InputRelation : InputBabiesInfoUiState()
    data class ModifyName(val babyInfo: BabyInfo, val position: Int) : InputBabiesInfoUiState()
    data class ModifyBirthday(val babyInfo: BabyInfo, val position: Int) : InputBabiesInfoUiState()
    data class ModifyHaveInviteCode(val position: Int) : InputBabiesInfoUiState()
    object InputInviteCode : InputBabiesInfoUiState()
    object CheckInviteCode : InputBabiesInfoUiState()
    object InputEndBabiesInfo : InputBabiesInfoUiState()
    object GetBabiesInfoByInviteCode : InputBabiesInfoUiState()
}
