package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BabyEditBottomSheetViewModel @Inject constructor() : ViewModel() {
    val title = MutableStateFlow("아이 그룹 이름")
    val button = MutableStateFlow("편집")
    val addBabyTitle = MutableStateFlow("아이 추가하기")
    val addBabyDescription = MutableStateFlow("직접 성장앨범을 촬영할 아이를 생성합니다.")
    val inputInviteCodeTitle = MutableStateFlow("초대코드 입력")
    val inputInviteCodeDescription = MutableStateFlow("초대받은 코드를 입력해 아이를 추가합니다.")
}