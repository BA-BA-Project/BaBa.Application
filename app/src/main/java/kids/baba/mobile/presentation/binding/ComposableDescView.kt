package kids.baba.mobile.presentation.binding

import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableDescView(
    val enabled: Boolean = true,
    val text: MutableStateFlow<String>
)
