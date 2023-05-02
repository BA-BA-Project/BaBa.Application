package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InputInviteCodeViewModel @Inject constructor(): ViewModel() {
    val title = MutableStateFlow("아이 추가하기")
    val bannerTitle = MutableStateFlow("초대코드를\n입력해 아이를 추가해봐요.")
    val bannerDesc = MutableStateFlow("지인의 아이를 추가하고 소통해봐요!")
    val inputTitle = MutableStateFlow("초대코드 입력하기")
}