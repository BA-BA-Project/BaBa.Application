package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.domain.usecase.GetBabiesInfoByInviteCodeUseCase
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.model.InviteMemberResultUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

    private val _eventFlow = MutableEventFlow<InviteResultEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        getInviteResult()
        registerMember()
    }

    private fun getInviteResult() = viewModelScope.launch {
        when (val babiesData = getBabiesInfoByInviteCodeUseCase(inviteCode.value)) {
            is Result.Success -> {
                _eventFlow.emit(InviteResultEvent.SuccessGetInvitationInfo(babiesData.data))
                uiModel.update {
                    it.copy(
                        tvGroupDescText = babiesData.data.relationGroup,
                        tvRelationDescText = babiesData.data.relationName
                    )
                }
            }
            is Result.NetworkError -> {
                _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }

    private fun registerMember() = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(InviteCode(inviteCode = inviteCode.value))

        when (val result = addOneBabyWithInviteCodeUseCase.add(InviteCode(inviteCode = inviteCode.value))) {
            is Result.Success -> {
                _eventFlow.emit(InviteResultEvent.SuccessAddMember)
            }
            is Result.NetworkError -> {
                _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }


    }

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(InviteResultEvent.GoToBack)
            }
        }
    )

    fun onCompleteClick() = viewModelScope.launch {
        _eventFlow.emit(InviteResultEvent.GoToMyPage)
    }

}