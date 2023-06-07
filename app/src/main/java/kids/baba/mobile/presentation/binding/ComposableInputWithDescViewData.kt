package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableInputWithDescViewData(
    val text: MutableStateFlow<String>,
    val onEditButtonClickEventListener: () -> Unit = {}
)
