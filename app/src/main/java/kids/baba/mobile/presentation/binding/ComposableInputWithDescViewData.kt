package kids.baba.mobile.presentation.binding

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

data class ComposableInputWithDescViewData(
    val text: MutableStateFlow/*MutableLiveData*/<String>
)
