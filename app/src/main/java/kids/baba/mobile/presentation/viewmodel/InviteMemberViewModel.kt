package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.domain.usecase.MakeInviteCodeUseCase
import kids.baba.mobile.presentation.model.InviteMemberUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val makeInviteCodeUseCase: MakeInviteCodeUseCase

) : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberUiModel())

}