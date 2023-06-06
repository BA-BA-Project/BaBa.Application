package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableNameViewData(
    val initialText: String = "",
    val enabled: Boolean = true,
    val maxLength: Int,
    val text: MutableStateFlow<String>,
    val onEditButtonClickEventListener: () -> Unit = {}
)