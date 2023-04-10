package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kids.baba.mobile.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
        .placeholder(R.drawable.album_default)
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
