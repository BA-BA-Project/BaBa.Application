package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.usecase.AddOneBabyWithInviteCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputInviteCodeViewModel @Inject constructor(
    private val addOneBabyWithInviteCodeUseCase: AddOneBabyWithInviteCodeUseCase
) : ViewModel() {
    val title = MutableStateFlow("아이 추가하기")
    val bannerTitle = MutableStateFlow("초대코드를\n입력해 아이를 추가해봐요.")
    val bannerDesc = MutableStateFlow("지인의 아이를 추가하고 소통해봐요!")
    val inputTitle = MutableStateFlow("초대코드 입력하기")

    fun add(inviteCode: String) = viewModelScope.launch {
        addOneBabyWithInviteCodeUseCase.add(inviteCode = InviteCode(inviteCode = inviteCode))
    }
}