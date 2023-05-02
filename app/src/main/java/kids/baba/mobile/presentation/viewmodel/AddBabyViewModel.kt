package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddBabyViewModel @Inject constructor() : ViewModel() {
    val title = MutableStateFlow("아이 추가하기")
    val bannerTitle = MutableStateFlow("내 아이를 추가해봐요")
    val bannerDesc = MutableStateFlow("직접 성장앨범을 기록할 아이를 만들어요.")
    val nameTitle = MutableStateFlow("아이 이름")
    val relationTitle = MutableStateFlow("나와 아이의 관계")
    val relationDesc = MutableStateFlow("내가 추가한 아이들와 나의 관계입니다.")
    val birthTitle = MutableStateFlow("아이 생일 혹은 출산 예정일")
}