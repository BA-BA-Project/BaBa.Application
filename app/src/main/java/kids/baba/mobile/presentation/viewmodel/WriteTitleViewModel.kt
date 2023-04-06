package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.content.res.Resources
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject


@HiltViewModel
class WriteTitleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources,
//    @ApplicationContext private val context: Context
) : ViewModel() {

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

    private val _eventFlow = MutableEventFlow<FilmEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun setPreviewImg(imageView: ImageView) {
        if (currentTakenMedia != null) {
            imageView.setImageURI(currentTakenMedia!!.mediaUri)
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
        if (currentTakenMedia != null) {

//            val fileName = currentTakenMedia!!.mediaUri.toString().split("/").last()
//            val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            val file = File(storageDir, fileName)

            currentTakenMedia = MediaData(
                mediaName = title.value,
                mediaDate = currentTakenMedia!!.mediaDate,
                mediaUri = currentTakenMedia!!.mediaUri
            )
            Log.d("WriteTitleViewModel", currentTakenMedia.toString())
        }
        emit(currentTakenMedia!!)
    }


    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}