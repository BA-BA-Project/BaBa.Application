package kids.baba.mobile.presentation.event

import kids.baba.mobile.presentation.model.CardStyleUiModel

sealed class CardSelectEvent {
    data class CardSelect(val card: CardStyleUiModel, val position: Int): CardSelectEvent()
}