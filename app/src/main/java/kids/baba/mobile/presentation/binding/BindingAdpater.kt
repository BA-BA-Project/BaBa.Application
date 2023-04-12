package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import kids.baba.mobile.R
import java.text.SimpleDateFormat
import java.util.*


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
        setAspectRatio(1, 1)
        setFixedAspectRatio(true)
        guidelines = CropImageView.Guidelines.ON
        cropShape = CropImageView.CropShape.RECTANGLE
        scaleType = CropImageView.ScaleType.FIT_CENTER
        isAutoZoomEnabled = false
        isShowProgressBar = true
    }
    cropImgView.setImageUriAsync(url.toUri())
}

@BindingAdapter("dateString")
fun setDateString(textView: TextView, nowDate: String) {
    if (nowDate == SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(System.currentTimeMillis())) {
        textView.setText(R.string.today)
    } else {
        textView.text = nowDate
    }
}