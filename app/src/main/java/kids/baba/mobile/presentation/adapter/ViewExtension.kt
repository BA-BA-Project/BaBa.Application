package kids.baba.mobile.presentation.adapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}


@BindingAdapter("imageSrc")
fun ImageView.setImage(src: String) {
    setImageURI(Uri.parse(src))
}



