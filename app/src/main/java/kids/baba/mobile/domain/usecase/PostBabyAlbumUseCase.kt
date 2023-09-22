package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.PostAlbumResponse
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.repository.AlbumRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostBabyAlbumUseCase @Inject constructor(private val albumRepository: AlbumRepository) {

    suspend fun postAlbum(
        photo: MultipartBody.Part,
        bodyDataHashMap: HashMap<String, RequestBody>
    ): Result<PostAlbumResponse> {

        val babyId = run { EncryptedPrefs.getBaby(PrefsKey.BABY_KEY).babyId }

        return albumRepository.postAlbum(id = babyId, photo, bodyDataHashMap)
    }

}