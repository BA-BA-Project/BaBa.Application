package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MediaData
import javax.inject.Inject


@HiltViewModel
class WriteTitleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {

    private var currentTakenMedia = savedStateHandle.get<MediaData>(MEDIA_DATA)

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
                } else {
                    button.isEnabled = true
                    button.setTextColor(resources.getColor(R.color.baba_main, null))
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}