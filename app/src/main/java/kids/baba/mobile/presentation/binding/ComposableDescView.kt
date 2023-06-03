package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableDescView(
    val text: MutableStateFlow<String>
)
