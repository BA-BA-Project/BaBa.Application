package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.usecase.AddOneGroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addOneGroupUseCase: AddOneGroupUseCase
) : ViewModel() {
    val topAppBarTitle = MutableStateFlow("그룹 만들기")
    val bannerTitle = MutableStateFlow("그룹을 만들어 소통을 관리해요!")
    val bannerDesc = MutableStateFlow("초대된 멤버는 자신의 그룹과\n가족 그룹의 댓글만 확인할 수 있어요")
    val nameViewTitle = MutableStateFlow("그룹 이름")
    val nameViewDesc = MutableStateFlow("어떤 그룹을 만들 건가요?")
    val colorViewTitle = MutableStateFlow("그룹 컬러")
    val permissionViewTitle = MutableStateFlow("성장앨범 업로드 권한")
    val permissionViewDesc = MutableStateFlow("없음")

    fun addGroup(relationGroup: String, iconColor: String) = viewModelScope.launch {
        addOneGroupUseCase.add(
            myPageGroup = MyPageGroup(
                relationGroup = relationGroup,
                iconColor = iconColor
            )
        )
    }
}