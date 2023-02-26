package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.error.UserNotFoundException
import kids.baba.mobile.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val tag = "MainViewModel"

    fun login(context: Context) {
        viewModelScope.launch {
            try {
                loginUseCase.login(context)
            } catch (e: Exception) {
                if (e is UserNotFoundException) {
                    Log.i(tag, "회원가입 토큰: ${e.signToken}")
                } else {
                    Log.e(tag, "에러: ${e.message}")
                }
            }
        }
    }
}
