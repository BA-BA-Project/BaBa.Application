package kids.baba.mobile.presentation.binding

data class ComposableDeleteViewData(
    val isFamily: Boolean = false,
    val onDeleteButtonClickEventListener: () -> Unit
)