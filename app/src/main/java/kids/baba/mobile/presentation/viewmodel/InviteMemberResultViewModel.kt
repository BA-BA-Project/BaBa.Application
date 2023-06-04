package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.domain.usecase.GetBabiesInfoByInviteCodeUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.model.InviteMemberResultUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteMemberResultViewModel @Inject constructor(
    private val getBabiesInfoByInviteCodeUseCase: GetBabiesInfoByInviteCodeUseCase,
    private val addOneBabyWithInviteCodeUseCase: AddOneBabyWithInviteCodeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberResultUiModel())
    val inviteCode = MutableStateFlow(savedStateHandle[MyPageFragment.INVITE_CODE] ?: "")
    val event = MutableEventFlow<InviteResultEvent>()

    init {
        Log.e("result", inviteCode.value)
        getInviteResult()
        registerMember()
    }

    private fun getInviteResult() = viewModelScope.launch {
        when(val inviteCode = getBabiesInfoByInviteCodeUseCase(inviteCode.value)) {
            is Result.Success ->  event.emit(InviteResultEvent.SuccessGetInvitationInfo(inviteCode.data))
            else -> {}
        }
    }

    fun registerMember() = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(InviteCode(inviteCode = inviteCode.value))
    }
}