package kids.baba.mobile.presentation.view.film

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kids.baba.mobile.R


class FilmDialog : DialogFragment() {

    private val tag = "FilmDialog"

    internal lateinit var listener: FilmDialogListener

    interface FilmDialogListener {
        fun onDialogCameraClick(dialog: DialogFragment)
        fun onDialogGalleryClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as FilmDialogListener
            Log.d(tag, listener.toString())
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement FilmDialogListener")
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(R.string.select_how_to_film)
                    .setItems(
                        R.array.how_to_film,
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                0 -> listener.onDialogCameraClick(this@FilmDialog)
                                1 -> listener.onDialogGalleryClick(this@FilmDialog)
                            }
                        }
                    )
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}
