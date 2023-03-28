package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import android.provider.MediaStore.Audio.Media
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WriteTitleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    private val _eventFlow = MutableEventFlow<FilmEvent>()
    val eventFlow = _eventFlow.asEventFlow()

//    private val _uiState: MutableStateFlow<InputBabiesInfoUiState> =
//        MutableStateFlow(InputBabiesInfoUiState.Loading)
//    val uiState = _uiState.asStateFlow()

    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun setPreviewImg(imageView: ImageView) {
        if (currentTakenMedia != null) {
            imageView.setImageURI(currentTakenMedia!!.mediaPath.toUri())
        }
    }

    fun setTitle(materialToolbar: MaterialToolbar) {
        if (currentTakenMedia != null) {
            materialToolbar.title = currentTakenMedia!!.mediaDate
        }
    }

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

                    _title.value = s.toString()

                } else {
                    button.isEnabled = true
                    button.setTextColor(resources.getColor(R.color.baba_main, null))
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    fun complete() = flow {
        if (currentTakenMedia != null) {
            currentTakenMedia = MediaData(
                mediaName = title.value,
                mediaPath = currentTakenMedia!!.mediaPath,
                mediaDate = currentTakenMedia!!.mediaDate
            )
            Log.e("WriteTitleViewModel", currentTakenMedia.toString())
        }
        emit(currentTakenMedia!!)
    }


    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}