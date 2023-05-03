package kids.baba.mobile.presentation.binding

import android.graphics.Color
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
    circleImageView.circleBackgroundColor = Color.parseColor(colorString)
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

@BindingAdapter("showYearAndMonth")
fun showYearAndMonth(textView: TextView, localDate: LocalDate) {
    val yearMonth = localDate.year.toString() + "." + localDate.monthValue.toString() + "월"
    textView.text = yearMonth
}

@BindingAdapter("showYear")
fun showYear(textView: TextView, localDate: LocalDate) {
    val year = localDate.year.toString() + "년"
    textView.text = year
}


//android:text="@{baby.gatheringAlbumUiModel.date.year}"