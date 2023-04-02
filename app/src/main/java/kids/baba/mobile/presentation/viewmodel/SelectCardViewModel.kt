package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.model.CardStyleIconUiModel
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.model.CardStyles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "SelectCardViewModel"

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    private var _cardState: MutableStateFlow<CardStyles> = MutableStateFlow(CardStyles(defaultCardIconList))
    val cardState = _cardState.asStateFlow()

    private var _cardPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val cardPosition = _cardPosition.asStateFlow()


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

    fun onCardSelected(position: Int) = viewModelScope.launch {
        Log.d(TAG, "selected position: $position")
        _cardPosition.value = position
    }


    private fun getCards() {
        val cardStyle = CardStyles(
            cardStyles = defaultCardIconList,
        )
        _cardState.value = cardStyle
    }


    companion object {
        const val MEDIA_DATA = "mediaData"

        private val defaultCardIconList = listOf(
            CardStyleUiModel(CardStyleIconUiModel.CARD_BASIC_1, "기본"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SKY_1, "하늘"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CLOUD_1, "구름1"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CLOUD_2, "구름2"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_TOY_1, "장난감"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CANDY_1, "알사탕"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SNOWFLOWER_1, "눈꽃1"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SNOWFLOWER_2, "눈꽃2"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_LINE_1, "스트라이프"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_SPRING_1, "스프핑"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CHECK_1, "체크1"),
            CardStyleUiModel(CardStyleIconUiModel.CARD_CHECK_2, "체크2"),
        )
    }


}