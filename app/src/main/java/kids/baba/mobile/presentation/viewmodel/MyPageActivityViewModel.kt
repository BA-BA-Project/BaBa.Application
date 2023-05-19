package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch

class MyPageActivityViewModel : ViewModel() {
    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun moveToAddBabyPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.StartAddBabyPage)
    }

    fun moveToInvitePage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.StartInvitePage)
    }

    fun moveToBabyDetailPage(baby: MemberUiModel) = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.StartBabyDetailPage(baby))
    }

    fun moveToInviteMemberPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.StartInviteMemberPage)
    }

    fun moveToSettingPage() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.StartSettingPage)
    }

    fun moveToAddGroupPage() = viewModelScope.launch {
        Log.e("moveTo", "AddGroupPage")
        _eventFlow.emit(MyPageEvent.StartAddGroupPage)
    }
}