package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import javax.inject.Inject

@HiltViewModel
class WriteTitleViewModel @Inject constructor(): ViewModel() {

    val currentTakenMedia = MutableLiveData<MediaData>()

    fun setArgument(args: Any) {
        currentTakenMedia.value = args as MediaData
    }



}