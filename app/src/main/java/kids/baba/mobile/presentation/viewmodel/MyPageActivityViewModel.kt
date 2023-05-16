package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyPageActivityViewModel : ViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val stateFlow = MutableStateFlow<MyPageEvent?>(null)
    fun moveToAddBabyPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.MoveToAddBabyPage)
    }

    fun moveToInvitePage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.MoveToInvitePage)
    }

    fun moveToBabyDetailPage(baby: MemberUiModel) = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.MoveToBabyDetailPage(baby))
    }

    fun moveToInviteMemberPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.MoveToInviteMemberPage)
    }

    fun moveToSettingPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.MoveToSettingPage)
    }

    fun moveToAddGroupPage() = viewModelScope.launch {
        Log.e("moveTo", "AddGroupPage")
        _eventFlow.emit(MyPageEvent.MoveToAddGroupPage)
        stateFlow.value = MyPageEvent.MoveToAddGroupPage
    }

    fun navigateUp() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.NavigateUp)
    }
}