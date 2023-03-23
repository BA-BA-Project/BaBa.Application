package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.CreateProfileEvent
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.ProfileIcon
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.state.CreateProfileUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val resources: Resources
) : ViewModel() {

    private val _createProfileUiState = MutableStateFlow<CreateProfileUiState>(CreateProfileUiState.Loading)
    val signUpUiState = _createProfileUiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<CreateProfileEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(listOf())
    val chatList = _chatList.asStateFlow().map { it.toList() }

    private var userName: String? = null
    private var userIcon: ProfileIcon? = null
    private var isAlreadyAskIcon = false

    private val _userProfile: MutableStateFlow<UserProfile?> = MutableStateFlow(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        addChat(
            ChatItem.BabaFirstChatItem(getStringResource(R.string.sign_up_greeting1))
        )
        addChat(
            ChatItem.BabaChatItem(getStringResource(R.string.sign_up_greeting2))
        )
        _createProfileUiState.value = CreateProfileUiState.SelectGreeting
    }

    private fun addChat(chatItem: ChatItem) {
        _chatList.value += chatItem
    }

    fun setEvent(event: CreateProfileEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun setUiState(uiState: CreateProfileUiState) {
        _createProfileUiState.value = uiState
    }

    fun setGreeting(chatItem: ChatItem.UserChatItem){
        addChat(chatItem)
        setUiState(CreateProfileUiState.Loading)
        checkProfile()
    }
    fun setUserName(chatItem: ChatItem.UserChatItem) {
        addChat(chatItem)
        userName = chatItem.message
        setUiState(CreateProfileUiState.Loading)
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
        setUiState(CreateProfileUiState.Loading)
        checkProfile()
    }

    private fun checkProfile() {
        val name = userName
        val icon = userIcon

        if (name.isNullOrEmpty()) {
            addChat(
                ChatItem.BabaFirstChatItem(getStringResource(R.string.please_input_name))
            )
            setUiState(CreateProfileUiState.InputName)
        } else if (icon == null) {
            if(isAlreadyAskIcon.not()){
                isAlreadyAskIcon = true
                addChat(
                    ChatItem.BabaFirstChatItem(getStringResource(R.string.please_select_profile_icon))
                )
                addChat(ChatItem.BabaChatSelectListItem(defaultProfileIconList))
                setUiState(CreateProfileUiState.SelectProfileIcon)
            }
        } else {
            _userProfile.value = UserProfile(name, icon.name)
            setUiState(CreateProfileUiState.EndCreateProfile)
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
        setUiState(CreateProfileUiState.Loading)
        checkProfile()
    }
    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

    companion object {

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