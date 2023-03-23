package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(): ViewModel(){

    private val _filmEventFlow = MutableEventFlow<FilmEvent>()
    val filmEventFlow = _filmEventFlow.asEventFlow()


    fun isMoveToCrop(mediaData: MediaData){
        viewModelScope.launch {
            _filmEventFlow.emit(FilmEvent.MoveToCrop(mediaData))
        }
    }

    fun isMoveToWriteTitleFromCrop(mediaData: MediaData) {
        viewModelScope.launch {
            _filmEventFlow.emit(FilmEvent.MoveToWriteTitleFromCrop(mediaData))
        }
    }

    fun isMoveToWriteTitleFromCamera(mediaData: MediaData) {
        viewModelScope.launch {
            _filmEventFlow.emit(FilmEvent.MoveToWriteTitleFromCamera(mediaData))
        }
    }

    fun isMoveToSelectCard(mediaData: MediaData) {
        viewModelScope.launch {
            _filmEventFlow.emit(FilmEvent.MoveToSelectCard(mediaData))
        }
    }





}