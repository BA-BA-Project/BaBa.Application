package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.film.FilmActivity
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _eventFlow = MutableEventFlow<FilmEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    fun savePhoto(path: String) = flow<FilmEvent> {
        viewModelScope.launch {
            val file = File(path)
            val mediaData =  MediaData(
                mediaName = file.name,
                mediaType = "IMAGE",
                mediaSize = file.length(),
                mediaPath = path
            )

            _eventFlow.emit(FilmEvent.MoveToWriteTitle(mediaData))
        }

    }





}