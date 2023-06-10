package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableNameViewData(
    val initialText: String = "",
    val maxLength: Int,
    val text: MutableStateFlow<String>,
    val onEditButtonClickEventListener: (Boolean) -> Unit = {}
)