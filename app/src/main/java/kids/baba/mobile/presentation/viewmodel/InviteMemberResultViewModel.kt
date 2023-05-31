package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.InviteMemberResultUiModel
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InviteMemberResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(InviteMemberResultUiModel())
    val inviteCode = MutableStateFlow(savedStateHandle[MyPageFragment.INVITE_CODE] ?: "")

    init {
        Log.e("result", inviteCode.value)
    }
}