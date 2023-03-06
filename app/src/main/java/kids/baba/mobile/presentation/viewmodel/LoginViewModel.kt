package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.core.error.kakao.KakaoLoginCanceledException
import kids.baba.mobile.domain.usecase.BabaLoginUseCase
import kids.baba.mobile.presentation.event.LoginEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val babaLoginUseCase: BabaLoginUseCase
) : ViewModel() {


    private val _eventFlow = MutableEventFlow<LoginEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun loginWithKakao() {
        viewModelScope.launch {
            babaLoginUseCase.login()
                .onFailure {
                    when (it) {
                        is KakaoLoginCanceledException -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.kakao_login_canceled))
                        is UserNotFoundException -> _eventFlow.emit(LoginEvent.MoveToAgree(it.signToken))
                        else -> _eventFlow.emit(LoginEvent.ShowSnackBar(R.string.baba_login_failed))
                    }
                }
        }
    }
}