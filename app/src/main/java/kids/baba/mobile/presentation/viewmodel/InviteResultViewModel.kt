package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.GetInvitationInfoUseCase
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.model.InviteResultUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteResultViewModel @Inject constructor(
    private val getInvitationInfoUseCase: GetInvitationInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(InviteResultUiModel())

    private val _eventFlow = MutableEventFlow<InviteResultEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val inviteCodeStr = savedStateHandle.get<String>("inviteCode").toString()

    init {
        Log.e("InviteResultViewModel", "init inviteCodeStr: $inviteCodeStr")
        viewModelScope.launch {
            when (val result = getInvitationInfoUseCase(inviteCode = inviteCodeStr)) {
                is Result.Success -> {
                    uiModel.update {
                        it.copy(
                            addBabyNameDesc = result.data.babies[0].babyName,
                            addBabyGroupDesc = result.data.relationGroup,
                            addBabyRelationDesc = result.data.relationName,
                            addBabyPermissionDesc = if (result.data.relationGroup == "가족") {
                                "있음"
                            } else {
                                "없음"
                            }
                        )
                    }
                    _eventFlow.emit(InviteResultEvent.SuccessGetInvitation)
                }
                is Result.NetworkError -> _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(InviteResultEvent.ShowSnackBar(R.string.unvalid_invite_code))
            }
        }
    }

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(InviteResultEvent.BackButtonClicked)
            }
        }
    )

    fun goToMyPage() {
        viewModelScope.launch {
            _eventFlow.emit(InviteResultEvent.GoToMyPage)
        }
    }

}