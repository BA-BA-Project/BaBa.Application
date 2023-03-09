package kids.baba.mobile.presentation.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(msgId: Int, length: Int) {
    showSnackbar(context.getString(msgId), length)
}

fun View.showSnackbar(msg: String, length: Int) {
    showSnackbar(msg, length, null, {})
}

fun View.showSnackbar(
    msgId: Int, length: Int, actionMessageId: Int, action: (View) -> Unit) {
    showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
}

fun View.showSnackbar(msg: String, length: Int, actionMessage: CharSequence?, action: (View) -> Unit) {
    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    }
}
