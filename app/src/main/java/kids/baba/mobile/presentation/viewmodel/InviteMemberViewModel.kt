package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.InviteMemberUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberUiModel())
}