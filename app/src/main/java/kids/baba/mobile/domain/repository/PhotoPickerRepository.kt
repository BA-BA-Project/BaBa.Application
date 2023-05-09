package kids.baba.mobile.domain.repository

import android.net.Uri
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.flow.Flow

interface PhotoPickerRepository {
    suspend fun getPhoto(uri: Uri): Flow<MediaData>
}