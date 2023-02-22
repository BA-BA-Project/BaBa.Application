package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.state.IntroUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(

) : ViewModel(){
    private var _uiState = MutableStateFlow<IntroUiState>(IntroUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkLogin()
    }

    private fun tempUseCase() : Result<Boolean>{
        return kotlin.runCatching { true }
    }

    private fun checkLogin() {
        viewModelScope.launch {
            delay(SPLASH_TIME)
            val isLoggedIn = tempUseCase().getOrThrow() //로그인 확인 usecase 적용
            if(isLoggedIn) {
                val isOnboarded = tempUseCase().getOrThrow() // 온보드 확인 usecase 적용
                _uiState.value =
                    if(false) IntroUiState.AlreadyLoggedIn else IntroUiState.NeedToOnboard
            } else {
                _uiState.value = IntroUiState.NeedToLogin
            }
        }
    }

    companion object {
        private const val SPLASH_TIME = 2000L
    }

}