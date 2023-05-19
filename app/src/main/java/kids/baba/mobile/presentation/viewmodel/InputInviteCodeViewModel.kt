package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kids.baba.mobile.presentation.model.InputInviteCodeUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputInviteCodeViewModel @Inject constructor(
    private val addOneBabyWithInviteCodeUseCase: AddOneBabyWithInviteCodeUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(InputInviteCodeUiModel())

    fun add(inviteCode: String) = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(inviteCode = InviteCode(inviteCode = inviteCode))
    }
}