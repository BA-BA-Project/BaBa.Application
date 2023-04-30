package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditMemberViewModel @Inject constructor(): ViewModel() {
    val nameTitle = MutableStateFlow("이름")
    val name = MutableStateFlow("이재임")
    val deleteTitle = MutableStateFlow("멤버 삭제하기")
    val delete = MutableStateFlow("삭제")
}