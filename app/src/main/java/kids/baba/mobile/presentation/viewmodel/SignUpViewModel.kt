package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.SignUpEvent
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.ProfileIcon
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

    private var userName: String? = null
    private var userIcon: ProfileIcon? = null
    private var isAlreadyAskIcon = false

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
        userName = chatItem.message
        setUiState(SignUpUiState.Loading)
        checkProfile()
    }

    fun setUserIcon(profileIcon: ProfileIcon, idx: Int, position: Int) {
        userIcon = profileIcon
        _chatList.value = List(_chatList.value.size) { nowPosition ->
            if (nowPosition == position) {
                val babaChatSelectListItem =
                    _chatList.value[position] as ChatItem.BabaChatSelectListItem
                val itemList = babaChatSelectListItem.iconList
                ChatItem.BabaChatSelectListItem(List(itemList.size) { nowIdx ->
                    if (idx == nowIdx) {
                        itemList[nowIdx].copy(selected = true)
                    } else {
                        itemList[nowIdx].copy(selected = false)
                    }
                })
            } else {
                _chatList.value[nowPosition]
            }
        }
        setUiState(SignUpUiState.Loading)
        checkProfile()
    }

    private fun checkProfile() {
        val name = userName
        val icon = userIcon

        if (name.isNullOrEmpty()) {
            setUiState(SignUpUiState.InputName)
        } else if (icon == null) {
            if(isAlreadyAskIcon.not()){
                isAlreadyAskIcon = true
                setUiState(SignUpUiState.SelectProfileIcon)
            }
        } else {
            _userProfile.value = UserProfile(name, icon.name)
            setUiState(SignUpUiState.EndCreateProfile)
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
        userName = newChatItem.message
        setUiState(SignUpUiState.Loading)
        checkProfile()
    }

    fun addProfileList() {
        addChat(ChatItem.BabaChatSelectListItem(defaultProfileIconList))
    }

    companion object {
        const val KEY_SIGN_TOKEN = "signToken"

        private val defaultProfileIconList = listOf(
            ProfileIcon("PROFILE_W_1", R.drawable.profile_w_1, false),
            ProfileIcon("PROFILE_W_2", R.drawable.profile_w_2, false),
            ProfileIcon("PROFILE_W_3", R.drawable.profile_w_3, false),
            ProfileIcon("PROFILE_W_4", R.drawable.profile_w_4, false),
            ProfileIcon("PROFILE_W_5", R.drawable.profile_w_5, false),
            ProfileIcon("PROFILE_M_1", R.drawable.profile_m_1, false),
            ProfileIcon("PROFILE_M_2", R.drawable.profile_m_2, false),
            ProfileIcon("PROFILE_M_3", R.drawable.profile_m_3, false),
            ProfileIcon("PROFILE_M_4", R.drawable.profile_m_4, false),
            ProfileIcon("PROFILE_M_5", R.drawable.profile_m_5, false),
            ProfileIcon("PROFILE_M_6", R.drawable.profile_m_6, false),
            ProfileIcon("PROFILE_G_1", R.drawable.profile_g_1, false),
            ProfileIcon("PROFILE_G_2", R.drawable.profile_g_2, false),
            ProfileIcon("PROFILE_G_3", R.drawable.profile_g_3, false),
            ProfileIcon("PROFILE_G_4", R.drawable.profile_g_4, false),
        )
    }
}