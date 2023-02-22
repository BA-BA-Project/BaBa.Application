package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.state.IntroUiState
import kotlinx.coroutines.delay
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
            delay(SPLASH_TIME)
            getMemberUseCase.getMe().catch {
                isLoggedIn = false
            }.collect {
                isLoggedIn = true
            }

            if (isLoggedIn) {
                val isOnboarded = EncryptedPrefs.getBoolean(PrefsKey.ON_BOARDING_KEY)
                _uiState.value = if(isOnboarded){
                    IntroUiState.AlreadyLoggedIn
                } else {
                    IntroUiState.NeedToOnboard
                }
            } else {
                _uiState.value = IntroUiState.NeedToOnboard
            }
        }
    }

    companion object {
        private const val SPLASH_TIME = 2000L
    }

}