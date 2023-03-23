package kids.baba.mobile.presentation.adapter

import android.net.Uri
import android.provider.MediaStore.Audio.Media
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.SavedStateHandle
import com.canhub.cropper.CropImageView
import kids.baba.mobile.domain.model.MediaData

@BindingAdapter("visibility")
fun View.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}


//@BindingAdapter("imageSrc")
//fun ImageView.setImage(src: String) {
//    setImageURI(Uri.parse(src))
//}

@BindingAdapter("cropSrc")
fun setCropFrame(cropImageView: CropImageView, src: String) {
    cropImageView.apply {
        setAspectRatio(1, 1)
        setFixedAspectRatio(true)
    }
    cropImageView.apply {
        guidelines = CropImageView.Guidelines.ON
        cropShape = CropImageView.CropShape.RECTANGLE
        scaleType = CropImageView.ScaleType.FIT_CENTER
        isAutoZoomEnabled = false
        isShowProgressBar = true
    }
    cropImageView.setImageUriAsync((Uri.parse(src)))

//            cropImageView.setImageUriAsync(args.mediaData.mediaPath.toUri())
}

//@BindingAdapter("setSavedImg")
//fun setPreviewImg(imageView: ImageView, savedStateHandle: SavedStateHandle) {
//    val temp = savedStateHandle.get<MediaData>("mediaData")
//    temp?.let { imageView.setImage(it.mediaPath) }
//}


//@BindingAdapter("textChangedListener")
//fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher?) {
//    editText.addTextChangedListener(textWatcher)
//}

