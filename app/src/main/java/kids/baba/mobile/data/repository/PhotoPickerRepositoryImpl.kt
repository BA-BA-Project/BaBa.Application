package kids.baba.mobile.data.repository

import android.net.Uri
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.domain.repository.PhotoPickerRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotoPickerRepositoryImpl @Inject constructor(
) : PhotoPickerRepository {

    override suspend fun getPhoto(uri: Uri) = flow {
        val mediaData = MediaData(
            mediaName = uri.toString(),
            mediaUri = uri.toString()
        )
        emit(mediaData)

    }

}