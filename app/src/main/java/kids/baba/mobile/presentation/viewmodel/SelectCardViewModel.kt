package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.EntityTooLargeException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.PostBabyAlbumUseCase
import kids.baba.mobile.presentation.event.PostAlbumEvent
import kids.baba.mobile.presentation.extension.FileUtil
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.state.PostAlbumState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Entity
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postBabyAlbumUseCase: PostBabyAlbumUseCase,
    private val fileUtil: FileUtil,
) : ViewModel() {

    private val TAG = "SelectCardViewModel"

    val currentTakenMedia = MutableStateFlow(savedStateHandle[MEDIA_DATA] ?: MediaData())

    private var _cardState: MutableStateFlow<Array<CardStyleUiModel>> = MutableStateFlow(CardStyleUiModel.values())
    val cardState = _cardState.asStateFlow()

    private var _cardPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val cardPosition = _cardPosition.asStateFlow()

    private val _eventFlow = MutableEventFlow<PostAlbumEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val nowDate: MutableStateFlow<String> = MutableStateFlow("")

    private val requestHashMap = hashMapOf<String, RequestBody>()

    private val babyId = run { EncryptedPrefs.getBaby(PrefsKey.BABY_KEY).babyId }

    init {
        getCards()
    }

    fun onCardSelected(position: Int) = viewModelScope.launch {
        Log.d(TAG, "selected position: $position")
        _cardPosition.value = position
    }

    private fun getCards() {
        val cardUiModelArray = defaultCardUiModelArray
        _cardState.value = cardUiModelArray
    }

    fun postAlbumClick() {
        viewModelScope.launch {
            postAlbum()
        }
    }

    private suspend fun postAlbum() {

        val file = File(fileUtil.optimizeBitmap(currentTakenMedia.value.mediaUri.toUri()))

        val requestPhotoFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val photoFile: MultipartBody.Part = MultipartBody.Part.createFormData("photo", "photo", requestPhotoFile)

        requestHashMap["date"] = nowDate.value.toPlainRequestBody()
        requestHashMap["title"] = currentTakenMedia.value.mediaName.toPlainRequestBody()
        requestHashMap["cardStyle"] = defaultCardUiModelArray[cardPosition.value].name.toPlainRequestBody()

        when(val result = postBabyAlbumUseCase.postAlbum(babyId, photoFile, requestHashMap)){
            is Result.Success -> {
                _eventFlow.emit(PostAlbumEvent.MoveToMain)
            }
            is Result.Failure -> {
                if (result.throwable is EntityTooLargeException) {
                    // 사진 업로드에 실패했습니다
                    _eventFlow.emit(PostAlbumEvent.ShowSnackBar(R.string.post_album_failed))
                } else {
                    Log.e(TAG, "Result.Failure (not EntityTooLargeException)")
                }
            }
            is Result.NetworkError -> {
                Log.e(TAG, "NetworkError")
                // 네트워크 연결을 확인해주세요
                _eventFlow.emit(PostAlbumEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                Log.e(TAG, "Unexpected Error")
                // 알 수 없는 오류로 사진 업로드에 실패했습니다.
                _eventFlow.emit(PostAlbumEvent.ShowSnackBar(R.string.post_album_unexpected_result))
            }

        }
    }

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())


    companion object {
        const val MEDIA_DATA = "mediaData"
        private val defaultCardUiModelArray = CardStyleUiModel.values()
    }


}

