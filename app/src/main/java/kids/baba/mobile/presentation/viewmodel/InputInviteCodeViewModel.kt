package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.domain.usecase.GetBabiesInfoByInviteCodeUseCase
import kids.baba.mobile.presentation.binding.ComposableInputViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.BabyInviteCodeEvent
import kids.baba.mobile.presentation.model.InputInviteCodeUiModel
import kids.baba.mobile.presentation.state.InputCodeState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputInviteCodeViewModel @Inject constructor(
    private val addOneBabyWithInviteCodeUseCase: AddOneBabyWithInviteCodeUseCase,
    private val getBabiesInfoByInviteCodeUseCase: GetBabiesInfoByInviteCodeUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(InputInviteCodeUiModel())
    val uiState = MutableStateFlow<InputCodeState>(InputCodeState.Idle)

    private val _eventFlow = MutableEventFlow<BabyInviteCodeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val inviteCodeLiveData = MutableLiveData("")

    val composableInviteCode = ComposableInputViewData(
        text = inviteCodeLiveData
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
                inviteCode = InviteCode(/*composableInviteCode.text.value.toString()*/inviteCodeLiveData.value.toString())
            )) {
                is Result.Success -> _eventFlow.emit(
                    BabyInviteCodeEvent.SuccessAddBabyWithInviteCode(
                        inviteCode = InviteCode(
                            composableInviteCode.text.value.toString()
                        )
                    )
                )
                is Result.NetworkError -> _eventFlow.emit(BabyInviteCodeEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(BabyInviteCodeEvent.ShowSnackBar(R.string.unvalid_invite_code))
            }

        }
    }


    fun add(inviteCode: String) = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(inviteCode = InviteCode(inviteCode = inviteCode))
        getBabiesInfoByInviteCodeUseCase.invoke(inviteCode).onSuccess {
            uiState.value = InputCodeState.LoadInfo(it)
        }
    }
}