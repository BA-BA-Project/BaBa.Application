package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.presentation.binding.ComposableInputViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.BabyInviteCodeEvent
import kids.baba.mobile.presentation.state.InputCodeState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputInviteCodeViewModel @Inject constructor(
    private val addOneBabyWithInviteCodeUseCase: AddOneBabyWithInviteCodeUseCase
) : ViewModel() {
    val uiState = MutableStateFlow<InputCodeState>(InputCodeState.Idle)

    private val _eventFlow = MutableEventFlow<BabyInviteCodeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _inviteCodeFocus = MutableStateFlow(false)
    val inviteCodeFocus = _inviteCodeFocus.asStateFlow()

    private val inviteCodeState = MutableStateFlow("")

    val composableInviteCode = ComposableInputViewData(
        text = inviteCodeState,
        maxLine = 1,
        maxLength = 6,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyInviteCodeEvent.InviteCodeInput(it))
            }
        }
    )

    val composableBackButton = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyInviteCodeEvent.BackButtonClicked)
            }
        }
    )

    fun addBabyWithCode() {
        viewModelScope.launch {
            when (addOneBabyWithInviteCodeUseCase.add(
                inviteCode = InviteCode(inviteCodeState.value)
            )) {
                is ApiResult.Success -> _eventFlow.emit(
                    BabyInviteCodeEvent.SuccessAddBabyWithInviteCode(
                        inviteCode = InviteCode(
                            inviteCodeState.value
                        )
                    )
                )

                is ApiResult.NetworkError -> _eventFlow.emit(BabyInviteCodeEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(BabyInviteCodeEvent.ShowSnackBar(R.string.invalid_invite_code))
            }

        }
    }

    fun changeInviteCodeFocus(hasFocus: Boolean) = _inviteCodeFocus.update { hasFocus }

}