package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kids.baba.mobile.domain.model.MediaData

class WriteTitleViewModel: ViewModel() {

    val currentTakenMedia = MutableLiveData<MediaData>()

    internal fun setArgument(args: Any) {
        currentTakenMedia.value = args as MediaData
    }



}