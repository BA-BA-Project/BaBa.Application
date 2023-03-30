package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kids.baba.mobile.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
        .placeholder(R.drawable.album_default)
        .into(imageView)
}

@BindingAdapter("date", "formatter")
fun setDate(textView: TextView, date: LocalDate, formatter: DateTimeFormatter){
    textView.text = date.format(formatter)
}
