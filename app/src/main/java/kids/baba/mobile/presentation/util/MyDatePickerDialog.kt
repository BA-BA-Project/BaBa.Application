package kids.baba.mobile.presentation.util

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import java.util.*

class MyDatePickerDialog(
    context: Context, private val listener: DatePickerDialog.OnDateSetListener,
    year: Int, month: Int, dayOfMonth: Int, private val listener2: () -> Unit
) : DatePickerDialog(context, listener, year, month, dayOfMonth) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.setOnClickListener {
            listener.onDateSet(datePicker, year, month, dayOfMonth)
            dismiss()
        }

        val negativeButton = getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton?.setOnClickListener {
            listener2()
            dismiss()
        }
    }
}