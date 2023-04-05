package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.MemberNotFoundException
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.repository.AlbumRepository
import kids.baba.mobile.presentation.mapper.toPresentation
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostBabyAlbumUseCase @Inject constructor(private val albumRepository: AlbumRepository) {
    private val tag = "PostBabyAlbumUseCase"

    suspend fun postAlbum(id: String, photo: MultipartBody.Part, bodyDataHashMap: HashMap<String, RequestBody>) = flow {
        runCatching { EncryptedPrefs.getMember(PrefsKey.MEMBER_KEY).toPresentation() }.getOrElse {
            if (it is MemberNotFoundException) {
                val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)
                if (accessToken.isEmpty()) {
                    val msg = "accessToken이 존재하지 않음."
                    Log.e(tag, msg)
                    throw TokenEmptyException(msg)
                } else {
                    albumRepository.postAlbum(accessToken, id, photo, bodyDataHashMap).collect { postAlbumResponse ->
                        emit(postAlbumResponse)
                    }
                }
            }
        }
    }

}