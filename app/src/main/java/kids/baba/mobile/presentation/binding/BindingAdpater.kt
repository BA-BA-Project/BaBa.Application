package kids.baba.mobile.presentation.binding

import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import kids.baba.mobile.R


@BindingAdapter("iconRes")
fun setIcon(imageView: ImageView, @DrawableRes res: Int) {
    imageView.setImageResource(res)
}


@BindingAdapter("visibility")
fun View.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("app:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}

@BindingAdapter("activeColor")
fun setActiveColor(view: AppCompatButton, @ColorInt color: Int) {
    view.setTextColor(color)
}

@BindingAdapter("titleTextWatcher")
fun titleTextWatcher(view: EditText, textWatcher: TextWatcher) {
    view.addTextChangedListener(textWatcher)
}