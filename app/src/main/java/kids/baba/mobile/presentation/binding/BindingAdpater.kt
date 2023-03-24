package kids.baba.mobile.presentation.binding

import android.graphics.Color
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
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
