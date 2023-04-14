package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
) : ViewModel() {
    val title = MutableStateFlow("앙쥬네 가족")
    val description = MutableStateFlow("모든 그룹과 소식을 공유할 수 있어요")
    val motherTitle = MutableStateFlow("외가")
    val motherDescription = MutableStateFlow("앙쥬네 가족, 외가의 소식만 볼 수 있어요")
    val fatherTitle = MutableStateFlow("친가")
    val fatherDescription = MutableStateFlow("앙쥬네 가족, 친가의 소식만 볼 수 있어요")
    val friendsTitle = MutableStateFlow("친구")
    val friendsDescription = MutableStateFlow("앙쥬네 가족, 친구의 소식만 볼 수 있어요")
    val groupAddButton = MutableStateFlow("+ 그룹만들기")
}