package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.presentation.event.DeepLinkEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    private val _deepLinkEvent = MutableEventFlow<DeepLinkEvent>()
    val deepLinkEvent = _deepLinkEvent.asEventFlow()

    fun handleDeeplink() = viewModelScope.launch {
        when (getMemberUseCase.getMe()) {
            is Result.Success -> _deepLinkEvent.emit(DeepLinkEvent.GoToInviteResultPage)
            is Result.Unexpected -> _deepLinkEvent.emit(DeepLinkEvent.RequestLogin)
            is Result.Failure -> _deepLinkEvent.emit(DeepLinkEvent.Failure)
            is Result.NetworkError -> _deepLinkEvent.emit(DeepLinkEvent.NetworkError)
        }
    }
}