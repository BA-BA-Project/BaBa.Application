package kids.baba.mobile.presentation.binding

import androidx.lifecycle.MutableLiveData

data class ComposableNameViewData(
    val text: MutableLiveData<String>,
    val onEditButtonClickEventListener: () -> Unit
)