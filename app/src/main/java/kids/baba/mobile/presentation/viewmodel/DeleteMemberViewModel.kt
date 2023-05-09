package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DeleteMemberViewModel @Inject constructor() : ViewModel() {
    val topAppBarTitle = MutableStateFlow("계정 삭제")
    val bannerTitle = MutableStateFlow("계정을 삭제하려는 이유가\n 무엇인가요?")
    val bannerDesc = MutableStateFlow("서비스 개선을 위해 선택해주세요.")
    val forDataDeleteContent = MutableStateFlow("데이터 삭제를 위해")
    val uncomfortableContent = MutableStateFlow("불편한 사용성으로 인해")
    val frequentErrorsContent = MutableStateFlow("잦은 오류로 인해")
    val notUseContent = MutableStateFlow("낮은 사용 빈도로 인해")
    val etcContent = MutableStateFlow("기타")
}