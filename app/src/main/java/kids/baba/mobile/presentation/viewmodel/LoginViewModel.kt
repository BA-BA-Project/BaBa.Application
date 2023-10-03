package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.core.error.kakao.KakaoLoginCanceledException
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.LoginUseCase
import kids.baba.mobile.presentation.event.LoginEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {


    private val _eventFlow = MutableEventFlow<LoginEvent>()
    val eventFlow = _eventFlow.asEventFlow()


    fun loginWithKakao() {
        viewModelScope.launch {
            loginUseCase.kakaoLogin().onSuccess {
                loginToBaba(it)
            }.onFailure {
                when (it) {
                    is KakaoLoginCanceledException -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.kakao_login_canceled))
                    else -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_login_failed))
                }
            }
        }
    }

    private suspend fun loginToBaba(socialToken: String) {
        when (val result = loginUseCase.babaLogin(socialToken)) {
            is ApiResult.Success -> {
                when(val memberResult = getMemberUseCase.getMe()){
                    is ApiResult.Success -> {
                        _eventFlow.emit(LoginEvent.MoveToWelcome(memberResult.data.name))
                    }
                    is ApiResult.NetworkError -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_network_failed))
                    else -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_get_member_failed))
                }
            }

            is ApiResult.Failure -> {
                if (result.throwable is UserNotFoundException) {
                    _eventFlow.emit(LoginEvent.MoveToAgree(socialToken))
                } else {
                    _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_login_failed))
                }
            }

            is ApiResult.NetworkError -> {
                _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_network_failed))
            }

            else -> {
                _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_login_failed))
            }
        }
    }
}