package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.state.IntroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<IntroUiState>(IntroUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var isLoggedIn = false

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            getMemberUseCase.getMe().catch {
                isLoggedIn = false
            }.collect {
                isLoggedIn = true
            }
            if (isLoggedIn) {
                _uiState.value = IntroUiState.AlreadyLoggedIn
            } else {
                _uiState.value = IntroUiState.NeedToOnboard
            }
        }
    }

    fun setOnBoardingEnd() {
        _uiState.value = IntroUiState.NeedToLogin
    }
}