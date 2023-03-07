package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.event.SignUpEvent
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.ChatUserType
import kids.baba.mobile.presentation.state.SignUpUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Loading)
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(listOf())
    val chatList = _chatList.asStateFlow().map { it.toList() }

    init {
        _signUpUiState.value = SignUpUiState.SelectGreeting
    }

    fun addChat(chatUserType: ChatUserType,message: String, canModify: Boolean){
        _chatList.value += ChatItem(chatUserType,message,canModify)
    }

    fun setEvent(event: SignUpEvent){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
    fun setUiState(uiState: SignUpUiState){
        _signUpUiState.value = uiState
    }
}