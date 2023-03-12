package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import java.io.File
import javax.inject.Inject

//@HiltViewModel
class CameraViewModel : ViewModel() {

    internal fun savePhoto(path: String): MediaData {
        val file = File(path)
        return MediaData(
            mediaName = file.name,
            mediaType = "IMAGE",
            mediaSize = file.length(),
            mediaPath = path
        )
    }

}