package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.domain.usecase.GetBabiesInfoByInviteCodeUseCase
import kids.baba.mobile.presentation.model.InputInviteCodeUiModel
import kids.baba.mobile.presentation.state.InputCodeState
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

    fun add(inviteCode: String) = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(inviteCode = InviteCode(inviteCode = inviteCode))
        getBabiesInfoByInviteCodeUseCase.invoke(inviteCode).onSuccess {
            uiState.value = InputCodeState.LoadInfo(it)
        }
    }
}