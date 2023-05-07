package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.state.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageGroupUseCase: GetMyPageGroupUseCase,
) : ViewModel() {
    val myName = MutableStateFlow("손제인")
    val myStatusMessage = MutableStateFlow("상테메시지 설정해요~")
    val title = MutableStateFlow("앙쥬네 가족")
    val description = MutableStateFlow("모든 그룹과 소식을 공유할 수 있어요")
    val motherTitle = MutableStateFlow("외가")
    val motherDescription = MutableStateFlow("앙쥬네 가족, 외가의 소식만 볼 수 있어요")
    val fatherTitle = MutableStateFlow("친가")
    val fatherDescription = MutableStateFlow("앙쥬네 가족, 친가의 소식만 볼 수 있어요")
    val friendsTitle = MutableStateFlow("친구")
    val friendsDescription = MutableStateFlow("앙쥬네 가족, 친구의 소식만 볼 수 있어요")
    val groupAddButton = MutableStateFlow("+ 그룹만들기")
    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Idle)
    val uiState = _uiState
    fun loadGroups() = viewModelScope.launch {
        getMyPageGroupUseCase.get().catch {

        }.collect {
            _uiState.value = MyPageUiState.LoadMember(it.groups[0].members)
            title.value = it.groups[0].groupName

            Log.e("group","$it")
        }
    }
}