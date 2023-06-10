package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableInputViewData(
    val initialText: String = "",
    val text: MutableStateFlow<String>,
    val maxLine: Int,
    val maxLength: Int,
    val onEditButtonClickEventListener: (Boolean) -> Unit = {}
)