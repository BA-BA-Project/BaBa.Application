package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.core.error.kakao.KakaoLoginCanceledException
import kids.baba.mobile.domain.usecase.BabaLoginUseCase
import kids.baba.mobile.presentation.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val babaLoginUseCase: BabaLoginUseCase
) : ViewModel() {

    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val loginUiState = _loginUiState.asStateFlow()

    fun loginWithKakao() {
        viewModelScope.launch {
            babaLoginUseCase.login().onFailure {
                _loginUiState.value = when (it) {
                    is KakaoLoginCanceledException -> LoginUiState.LoginCanceled
                    is UserNotFoundException -> LoginUiState.NeedToSignUp(it.signToken)
                    else -> LoginUiState.Failure
                }
            }
        }
    }
}