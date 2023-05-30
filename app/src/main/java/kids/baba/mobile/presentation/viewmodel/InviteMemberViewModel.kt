package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.binding.ComposableInputWithDescViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.model.InviteMemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberUiModel())

    private val relationState = MutableStateFlow("")

    private val _eventFlow = MutableEventFlow<InviteMemberEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(InviteMemberEvent.GoToBack)
            }
        }
    )

    val relationWithBaby = ComposableInputWithDescViewData(
        text = relationState
    )

    fun copyInviteCode() {
        viewModelScope.launch {

            _eventFlow.emit(InviteMemberEvent.CopyInviteCode)
        }
    }

    fun inviteWithKakao() {
        viewModelScope.launch {
            _eventFlow.emit(InviteMemberEvent.InviteWithKakao)
        }
    }

}