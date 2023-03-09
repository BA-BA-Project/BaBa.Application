package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.InputBabiesInfoEvent
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.state.InputChildrenInfoUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputBabiesInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {

    private val userProfile = savedStateHandle.get<UserProfile>(KEY_USER_PROFILE)

    private val _uiState: MutableStateFlow<InputChildrenInfoUiState> =
        MutableStateFlow(InputChildrenInfoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<InputBabiesInfoEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(listOf())
    val chatList = _chatList.asStateFlow().map { it.toList() }


    private val _haveInviteCode = MutableStateFlow(false)
    val haveInviteCode = _haveInviteCode.asStateFlow()

    init {
        checkInviteCode()
    }


    private fun checkInviteCode() {
        addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.check_have_invite_code)))
        setEvent(InputBabiesInfoEvent.SelectHaveInviteCode)
    }


    private fun addChat(chatItem: ChatItem) {
        _chatList.value += chatItem
    }

    fun setUiState(uiState: InputChildrenInfoUiState) {
        _uiState.value = uiState
    }

    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

    private fun setEvent(event: InputBabiesInfoEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun setHaveInviteCode(haveInviteCode: Boolean) {
        _haveInviteCode.value = haveInviteCode
        val answerRes = if (haveInviteCode) R.string.answer_yes else R.string.answer_no
        addChat(
            ChatItem.UserChatItem(
                getStringResource(answerRes),
                UserChatType.SELECTION_HAVE_INVITE_CODE,
                canModify = true,
                isModifying = false
            )
        )
    }

    fun setInputEnd() {
        setEvent(InputBabiesInfoEvent.InputEnd)
    }

    companion object {
        const val KEY_USER_PROFILE = "userProfile"
    }

}
