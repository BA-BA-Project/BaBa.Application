package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.event.DeepLinkEvent
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<IntroEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _deepLinkEvent = MutableEventFlow<DeepLinkEvent>()
    val deepLinkEvent = _deepLinkEvent.asEventFlow()

    suspend fun checkLogin() = getMemberUseCase.getMe() is ApiResult.Success
    fun handleDeeplink() = viewModelScope.launch {
        when (getMemberUseCase.getMe()) {
            is ApiResult.Success -> _deepLinkEvent.emit(DeepLinkEvent.GoToInviteResultPage)
            is ApiResult.Unexpected -> _deepLinkEvent.emit(DeepLinkEvent.RequestLogin)
            is ApiResult.Failure -> _deepLinkEvent.emit(DeepLinkEvent.Failure)
            is ApiResult.NetworkError -> _deepLinkEvent.emit(DeepLinkEvent.NetworkError)
        }
    }

    fun isOnBoardingEnd() {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToLogin)
        }
    }

    fun isLoginSuccess(name: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToWelcome(name))
        }
    }

    fun isSignUpStart(signToken: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToCreateUserProfile(signToken))
        }
    }

    fun isNeedToBabiesInfo(signToken: String, userProfile: UserProfile) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToInputBabiesInfo(signToken, userProfile))
        }
    }

    fun isNeedToAgree(socialToken: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToAgree(socialToken))
        }
    }

    fun isSignUpSuccess(name: String) {
        viewModelScope.launch {
            _eventFlow.emit(IntroEvent.MoveToWelcome(name))
        }
    }
}