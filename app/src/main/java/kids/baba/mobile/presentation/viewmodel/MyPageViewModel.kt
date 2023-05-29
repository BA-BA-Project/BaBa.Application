package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.getThrowableOrNull
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.state.MyPageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
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

        when (val result = getBabiesUseCase()) {
            is Result.Success -> {
                val babies = result.data
                _uiState.value = MyPageUiState.LoadBabies(babies.myBaby + babies.others)
            }

            is Result.NetworkError -> _uiState.value = MyPageUiState.Error(result.throwable)
            else -> {
                val throwable = result.getThrowableOrNull()
                if(throwable != null) {
                    _uiState.value = MyPageUiState.Error(throwable)
                }
            }
        }
    }

    fun getMyInfo() = viewModelScope.launch {
        when(val result = getMemberUseCase.getMeNoPref()){
            is Result.Success -> _uiState.value = MyPageUiState.LoadMyInfo(result.data.toPresentation())
            is Result.NetworkError -> _uiState.value = MyPageUiState.Error(result.throwable)
            else -> {
                val throwable = result.getThrowableOrNull()
                if(throwable != null) {
                    _uiState.value = MyPageUiState.Error(throwable)
                }
            }
        }
    }
}