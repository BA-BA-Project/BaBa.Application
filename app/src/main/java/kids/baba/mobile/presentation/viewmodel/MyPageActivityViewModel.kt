package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch

class MyPageActivityViewModel : ViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun moveToCompleteAddBaby() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.CompleteAddBaby)
    }
}