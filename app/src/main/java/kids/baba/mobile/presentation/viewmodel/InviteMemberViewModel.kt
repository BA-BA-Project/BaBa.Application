package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.MakeInviteCodeUseCase
import kids.baba.mobile.presentation.binding.ComposableDescView
import kids.baba.mobile.presentation.binding.ComposableInputWithDescViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val makeInviteCodeUseCase: MakeInviteCodeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val groupName = MutableStateFlow(savedStateHandle[GROUP_NAME] ?: "")
    private val _eventFlow = MutableEventFlow<InviteMemberEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val relationState = MutableStateFlow("")

    private val groupNameState = MutableStateFlow(savedStateHandle[GROUP_NAME] ?: "")

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(InviteMemberEvent.GoToBack)
            }
        }
    )

    val relationGroup = ComposableDescView(
        text = groupNameState,
        enabled = false
    )

    val relationWithBaby = ComposableInputWithDescViewData(
        text = relationState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(InviteMemberEvent.InputEnd)
            }
        }
    )

    fun copyInviteCode() {
        viewModelScope.launch {
            when (val inviteCode = makeInviteCodeUseCase(
                relationInfo = RelationInfo(
                    relationGroup = groupNameState.value,
                    relationName = relationState.value
                )
            )) {
                is Result.Success -> _eventFlow.emit(InviteMemberEvent.CopyInviteCode(inviteCode.data))

                is Result.NetworkError -> _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.baba_network_failed))

                else -> _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.cannot_generate_invite_code))
            }


        }
    }

    fun inviteWithKakao() {
        viewModelScope.launch {
            when (val inviteCode = makeInviteCodeUseCase(
                relationInfo = RelationInfo(
                    relationGroup = groupNameState.value,
                    relationName = relationState.value
                )
            )) {
                is Result.Success -> _eventFlow.emit(InviteMemberEvent.InviteWithKakao(inviteCode.data))

                is Result.NetworkError -> _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.baba_network_failed))

                else -> _eventFlow.emit(InviteMemberEvent.ShowSnackBar(R.string.cannot_generate_invite_code))
            }
        }
    }
}