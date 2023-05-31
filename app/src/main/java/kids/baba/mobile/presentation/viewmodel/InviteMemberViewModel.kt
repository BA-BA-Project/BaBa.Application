package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.domain.usecase.MakeInviteCodeUseCase
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.model.InviteMemberUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val makeInviteCodeUseCase: MakeInviteCodeUseCase

) : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberUiModel())

    private val _eventFlow = MutableEventFlow<InviteMemberEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun copyCode(relationInfo: RelationInfo) = viewModelScope.launch {
        when (val inviteCode = makeInviteCodeUseCase(relationInfo)) {
            is kids.baba.mobile.domain.model.Result.Success -> {
                Log.e("InviteMemberViewModel", "copyCode: Success")
                _eventFlow.emit(InviteMemberEvent.SuccessCopyInviteCode(inviteCode.data))
            }

            is kids.baba.mobile.domain.model.Result.NetworkError -> {
                _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.baba_network_failed))
            }

            else -> {
                _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }

    fun sendInvitation(relationInfo: RelationInfo) = viewModelScope.launch {
        when (val inviteCode = makeInviteCodeUseCase(relationInfo)) {
            is kids.baba.mobile.domain.model.Result.Success -> {
                Log.e("InviteMemberViewModel", "copyCode: Success")
                _eventFlow.emit(InviteMemberEvent.SuccessSendInvitation(inviteCode.data))
            }

            is kids.baba.mobile.domain.model.Result.NetworkError -> {
                _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.baba_network_failed))
            }

            else -> {
                _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }


}