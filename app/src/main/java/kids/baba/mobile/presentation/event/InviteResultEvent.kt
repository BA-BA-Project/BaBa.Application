package kids.baba.mobile.presentation.event

import kids.baba.mobile.domain.model.BabiesInfoResponse

sealed class InviteResultEvent{
    data class Success(val data: BabiesInfoResponse): InviteResultEvent()
}
