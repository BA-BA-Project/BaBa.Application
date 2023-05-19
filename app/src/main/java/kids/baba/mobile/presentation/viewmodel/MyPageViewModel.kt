package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.state.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageGroupUseCase: GetMyPageGroupUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    val groupAddButton = MutableStateFlow("+ 그룹만들기")


    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Idle)
    val uiState = _uiState

    fun loadGroups() = viewModelScope.launch {
        getMyPageGroupUseCase.get().catch {

        }.collect {
            _uiState.value = MyPageUiState.LoadMember(it.groups)
        }
    }

    fun loadBabies() = viewModelScope.launch {
        getBabiesUseCase.getBabies().catch { }.collect {
            _uiState.value = MyPageUiState.LoadBabies(it.myBaby + it.others)
        }
    }

    fun getMyInfo() = viewModelScope.launch {
        getMemberUseCase.getMeNoPref().map { it.toPresentation() }.collect {
            _uiState.value = MyPageUiState.LoadMyInfo(it)
        }
    }
}