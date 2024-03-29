package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageView
import com.google.android.material.appbar.MaterialToolbar
import de.hdodenhof.circleimageview.CircleImageView
import kids.baba.mobile.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@BindingAdapter("iconRes")
fun setIcon(imageView: ImageView, @DrawableRes res: Int) {
    imageView.setImageResource(res)
}

@BindingAdapter("backGroundColor")
fun setBackGroundColor(circleImageView: CircleImageView, colorString: String?) {
    circleImageView.circleBackgroundColor = Color.parseColor(colorString ?: "#FFFFFF")
}

@BindingAdapter("imageFromUrl")
fun setImageFromUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(R.drawable.last_album)
        .into(imageView)
}

@BindingAdapter("cameraImageFromUrl")
fun setCameraImageFromUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}

@BindingAdapter("date", "formatter")
fun setDate(textView: TextView, date: LocalDate, formatter: DateTimeFormatter) {
    textView.text = date.format(formatter)
}

@BindingAdapter("date")
fun setDayOfWeek(textView: TextView, date: LocalDate) {
    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.SUNDAY -> R.string.sunday
        DayOfWeek.MONDAY -> R.string.monday
        DayOfWeek.TUESDAY -> R.string.tuesday
        DayOfWeek.WEDNESDAY -> R.string.wednesday
        DayOfWeek.THURSDAY -> R.string.thursday
        DayOfWeek.FRIDAY -> R.string.friday
        else -> R.string.saturday
    }
    textView.text = textView.context.getText(dayOfWeek)
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


@BindingAdapter("dateTitle", "formatter")
fun setTitleDate(materialToolbar: MaterialToolbar, date: LocalDate, formatter: DateTimeFormatter) {
    materialToolbar.title = date.format(formatter)
}

