package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("iconRes")
fun setIcon(imageView: ImageView, @DrawableRes res: Int) {
    imageView.setImageResource(res)
}

@BindingAdapter("backGroundColor")
fun setBackGroundColor(circleImageView: CircleImageView, colorString: String) {
    circleImageView.circleBackgroundColor = Color.parseColor(colorString)
}

@BindingAdapter("imageFromUrl")
fun setImageFromUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}


@BindingAdapter("cardBackground")
fun setBackGround(view: View, @DrawableRes res: Int) {
    view.setBackgroundResource(res)
}

@BindingAdapter("cropImageView")
fun setCropImgView(cropImgView: CropImageView, url: String) {
    cropImgView.apply {
        setAspectRatio(1,1)
        setFixedAspectRatio(true)
        guidelines = CropImageView.Guidelines.ON
        cropShape = CropImageView.CropShape.RECTANGLE
        scaleType = CropImageView.ScaleType.FIT_CENTER
        isAutoZoomEnabled = false
        isShowProgressBar = true
    }
    cropImgView.setImageUriAsync(url.toUri())
}
