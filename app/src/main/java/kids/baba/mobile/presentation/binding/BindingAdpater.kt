package kids.baba.mobile.presentation.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("iconRes")
fun setIcon(imageView: ImageView, @DrawableRes res: Int){
    imageView.setImageResource(res)
}

@BindingAdapter("imageFromUrl")
fun setImageFromUrl(imageView: ImageView, url: String){
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}
