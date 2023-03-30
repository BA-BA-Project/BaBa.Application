package kids.baba.mobile.presentation.binding

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter


@BindingAdapter("iconRes")
fun setIcon(imageView: ImageView, @DrawableRes res: Int) {
    imageView.setImageResource(res)
}


@BindingAdapter("visibility")
fun View.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}