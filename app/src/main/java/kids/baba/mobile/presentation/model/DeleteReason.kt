package kids.baba.mobile.presentation.model

import androidx.annotation.StringRes

data class DeleteReason(
    @StringRes
    val reason: Int,
    val isChecked: Boolean
)
