package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.event.SignUpEvent
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.state.SignUpUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Loading)
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<SignUpEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(listOf())
    val chatList = _chatList.asStateFlow().map { it.toList() }

    val signToken = savedStateHandle[KEY_SIGN_TOKEN] ?: ""

    private val _userName: MutableStateFlow<String?> = MutableStateFlow(null)
    val userName = _userName.asStateFlow()

    private val _userIcon: MutableStateFlow<String?> = MutableStateFlow(null)
    val userIcon = _userIcon.asStateFlow()

    private val _userProfile: MutableStateFlow<UserProfile?> = MutableStateFlow(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        _signUpUiState.value = SignUpUiState.SelectGreeting
    }

    fun addChat(chatItem: ChatItem) {
        _chatList.value += chatItem
    }

    fun setEvent(event: SignUpEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun setUiState(uiState: SignUpUiState) {
        _signUpUiState.value = uiState
    }

    fun setUserName(chatItem: ChatItem.UserChatItem) {
        addChat(chatItem)
        _userName.value = chatItem.message
        checkProfile()
    }

    private fun checkProfile() {
        if (_userName.value.isNullOrEmpty()) {
            _signUpUiState.value = SignUpUiState.InputName
        } else if (_userIcon.value.isNullOrEmpty()) {
            _signUpUiState.value = SignUpUiState.SelectProfile
        } else {
            setEvent(SignUpEvent.EndCreateProfile)
        }
    }

    fun changeModifyState(position: Int) {
        _chatList.value = List(_chatList.value.size) { idx ->
            if (idx == position) {
                val modifyChatItem = _chatList.value[idx] as ChatItem.UserChatItem
                modifyChatItem.copy(isModifying = true)
            } else {
                _chatList.value[idx]
            }
        }
    }

    fun modifyName(newChatItem: ChatItem.UserChatItem, position: Int) {
        _chatList.value = List(_chatList.value.size) { idx ->
            if (idx == position) {
                newChatItem
            } else {
                _chatList.value[idx]
            }
        }
        _userName.value = newChatItem.message
        checkProfile()
    }

    companion object {
        const val KEY_SIGN_TOKEN = "signToken"
    }
}