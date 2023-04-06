package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@HiltViewModel
class WriteTitleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources,
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<FilmEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()

    val currentTakenMedia = MutableStateFlow<MediaData?>(savedStateHandle[MEDIA_DATA])

    fun onTextChanged(editable: EditText, button: Button) {
        textChangedListener(editable, button)
        editable.requestFocus()
    }

    private fun textChangedListener(editable: EditText, button: Button) {
        editable.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editable.text.isEmpty()) {
                    button.isEnabled = false
                    button.setTextColor(resources.getColor(R.color.inactive_text, null))
                } else {
                    button.isEnabled = true
                    button.setTextColor(resources.getColor(R.color.baba_main, null))
                    _title.value = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun complete() = flow {

        currentTakenMedia.value = MediaData(
            mediaName = title.value,
            mediaDate = currentTakenMedia.value?.mediaDate.toString(),
            mediaUri = currentTakenMedia.value?.mediaUri ?: ""
        )
        emit(currentTakenMedia.value)
    }

    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}