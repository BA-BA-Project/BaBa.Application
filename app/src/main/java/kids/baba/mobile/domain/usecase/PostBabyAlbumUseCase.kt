package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostBabyAlbumUseCase @Inject constructor(private val albumRepository: AlbumRepository) {
    private val tag = "PostBabyAlbumUseCase"

    suspend fun postAlbum(id: String, photo: MultipartBody.Part, bodyDataHashmap: HashMap<String, RequestBody>) = flow {
        val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)
        if (accessToken.isEmpty()) {
            val msg = "accessToken 이 존재하지 않음"
            Log.e(tag, msg)
            throw TokenEmptyException(msg)
        } else {
            albumRepository.postAlbum(accessToken, id, photo, bodyDataHashmap).catch {
                throw it
            }.collect { postAlbumResponse ->
                emit(postAlbumResponse)
            }
        }
    }

}