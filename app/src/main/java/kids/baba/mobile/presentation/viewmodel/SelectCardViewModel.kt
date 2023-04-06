package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.usecase.PostBabyAlbumUseCase
import kids.baba.mobile.presentation.model.CardStyleUiModel
import kids.baba.mobile.presentation.state.PostAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.EOFException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postBabyAlbumUseCase: PostBabyAlbumUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = "SelectCardViewModel"

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    private var _cardState: MutableStateFlow<Array<CardStyleUiModel>> = MutableStateFlow(CardStyleUiModel.values())
    val cardState = _cardState.asStateFlow()

    private var _cardPosition: MutableStateFlow<Int> = MutableStateFlow(0)
    val cardPosition = _cardPosition.asStateFlow()

    private val _postAlbumState = MutableStateFlow<PostAlbumState>(PostAlbumState.UnInitialized)
    val postAlbumState = _postAlbumState.asStateFlow()

    // TODO: "memberId":"KAKAO2695099524","name":"poi" 인 계정에서의 아이로 테스트했음
    //  이 계정의 babyId 는 아래임. 추후에 SharedPreference 에
    //  사용자가 보고있는 babyId 을 저장하고 이를 불러올 것임.
    val babyId = "1c642535-12db-416b-ae0f-46070036a752"

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
        val cardUiModelArray = defaultCardUiModelArray
        _cardState.value = cardUiModelArray
    }

    fun postAlbumClick() {
        viewModelScope.launch {
            postAlbum()
        }
    }

    private suspend fun postAlbum() {

        if (currentTakenMedia != null) {
            Log.e(TAG, "url: ${currentTakenMedia!!.mediaUri}")
//            val file = UriUtil.toFile(context, currentTakenMedia!!.mediaUri) // 갤러리에서는 이렇게 해야 함.
            val file  = File(currentTakenMedia!!.mediaUri.toString()) // 카메라에서는 이렇게

            Log.e(TAG, "file: $file")
            val requestPhotoFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val photoFile: MultipartBody.Part = MultipartBody.Part.createFormData("photo", "photo", requestPhotoFile)

            val requestHashMap = hashMapOf<String, RequestBody>()
            // TODO: 날짜 변경하기
//            requestHashMap["date"] = currentTakenMedia!!.mediaDate.toPlainRequestBody()
            requestHashMap["date"] = "2023-03-09".toPlainRequestBody()
            requestHashMap["title"] = currentTakenMedia!!.mediaName.toPlainRequestBody()
            requestHashMap["cardStyle"] = defaultCardUiModelArray[cardPosition.value].name.toPlainRequestBody()

            postBabyAlbumUseCase.postAlbum(babyId, photoFile, requestHashMap)/*.catch {
                _postAlbumState.value = PostAlbumState.Error(it)
            }*/.collect {
                _postAlbumState.value = PostAlbumState.Success
            }
        }
    }

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

    companion object {
        const val MEDIA_DATA = "mediaData"
        private val defaultCardUiModelArray = CardStyleUiModel.values()
    }

    object UriUtil {
        // URI -> File
        fun toFile(context: Context, uri: Uri): File {
            val fileName = getFileName(context, uri)
            val file = FileUtil.createTempFile(context, fileName)
            FileUtil.copyToFile(context, uri, file)

            return File(file.absolutePath)
        }

        // get file name & extension
        private fun getFileName(context: Context, uri: Uri): String {
            val name = uri.toString().split("/").last()
            val ext = context.contentResolver.getType(uri)!!.split("/").last()

            return "$name.$ext"
        }
    }

    object FileUtil {
        // 임시 파일 생성
        fun createTempFile(context: Context, fileName: String): File {
            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(storageDir, fileName)
        }

        // 파일 내용 스트림 복사
        fun copyToFile(context: Context, uri: Uri, file: File) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = inputStream!!.read(buffer)
                if (byteCount < 0) break
                outputStream.write(buffer, 0, byteCount)
            }

            outputStream.flush()
            inputStream.close()
        }
    }

}

