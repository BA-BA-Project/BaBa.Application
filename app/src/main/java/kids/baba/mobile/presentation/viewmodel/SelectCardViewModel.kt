package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.CardSelectEvent
import kids.baba.mobile.presentation.model.CardStyleIconUiModel
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.model.CardStyles
import kids.baba.mobile.presentation.model.UserProfile
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    resources: Resources
) : ViewModel() {

    private val TAG = "SelectCardViewModel"

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    var _cardState: MutableStateFlow<CardStyles> = MutableStateFlow(CardStyles(defaultCardIconList, 0))
    val cardState = _cardState.asStateFlow()

    var _cardPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val cardPosition = _cardPosition.asStateFlow()

    private val cardSelectEventChannel = Channel<CardSelectEvent>()

    init {
        getCards()
    }

    fun setPreviewImg(imageView: ImageView) {
        if (currentTakenMedia != null) {
            imageView.setImageURI(currentTakenMedia!!.mediaPath.toUri())
        }
    }

    fun setTitle(title: TextView) {
        if (currentTakenMedia != null) {
            title.text = currentTakenMedia!!.mediaName
        }
    }

    fun onCardSelected(card: CardStyleUiModel, position: Int) = viewModelScope.launch {
        Log.e(TAG, "selected position: $position")
        _cardState.value.selected = position
        _cardPosition.value = position
        _cardState.value.cardStyles.get(position).isSelected = true
//        cardSelectEventChannel.send(CardSelectEvent.CardSelect(card, position))
    }


    private fun getCards() {
        val cardStyle = CardStyles(
            cardStyles = defaultCardIconList,
            selected = 0
        )
        _cardState.value = cardStyle
    }


    companion object {
        const val MEDIA_DATA = "mediaData"

        private val defaultCardIconList = listOf(
            CardStyleUiModel(CardStyleIconUiModel.CARD_BASIC_1, "기본", true),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SKY_1, "하늘", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CLOUD_1, "구름1", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CLOUD_2, "구름2", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_TOY_1, "장난감", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CANDY_1, "알사탕", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SNOWFLOWER_1, "눈꽃1", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SNOWFLOWER_2, "눈꽃2", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_LINE_1, "스트라이프", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SPRING_1, "스프핑", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CHECK_1, "체크1", false),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CHECK_2, "체크2", false),
        )
    }


}