package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MediaData
import javax.inject.Inject

@HiltViewModel
class CropViewModel @Inject constructor(): ViewModel() {

    val currentTakenMedia = MutableLiveData<MediaData>()

    internal fun setArgument(args: Any) {
        currentTakenMedia.value = args as MediaData
    }

}