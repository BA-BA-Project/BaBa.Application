package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor() : ViewModel() {
    val name = MutableStateFlow("아이 이름")
    val intro = MutableStateFlow("내 소개")
    val bgColor = MutableStateFlow("배경 컬러")
}