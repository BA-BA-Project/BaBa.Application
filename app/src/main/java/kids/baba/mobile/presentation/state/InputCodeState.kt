package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.BabiesInfoResponse

sealed class InputCodeState {
    object Idle : InputCodeState()

    data class LoadInfo(val data: BabiesInfoResponse) : InputCodeState()
}
