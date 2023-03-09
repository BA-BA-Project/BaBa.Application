package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.InputBabiesInfoEvent
import kids.baba.mobile.presentation.model.Baby
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.model.UserProfile
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InputBabiesInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {

    private val userProfile = savedStateHandle.get<UserProfile>(KEY_USER_PROFILE)

    private val _uiState: MutableStateFlow<InputBabiesInfoUiState> =
        MutableStateFlow(InputBabiesInfoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableEventFlow<InputBabiesInfoEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatList = _chatList.asStateFlow().map { it.toList() }

    private val _babiesList = MutableStateFlow<List<Baby>>(emptyList())
    private val babiesList = _babiesList.asStateFlow()

    private val relation: String? = null

    private var nowBabyName: String? = null
    private var nowBabyBirth: LocalDate? = null

    private var inputMoreBaby = true

    private val _haveInviteCode: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val haveInviteCode = _haveInviteCode.asStateFlow()

    init {
        checkInviteCode()
    }


    private fun checkInviteCode() {
        addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.check_have_invite_code)))
        setEvent(InputBabiesInfoEvent.SelectHaveInviteCode)
    }

    fun inputBabiesInfo() {
        inputMoreBaby = true

        addChat(
            ChatItem.BabaFirstChatItem(
                getStringResource(R.string.need_to_baby_info)
            )
        )
        addChat(
            ChatItem.BabaFirstChatItem(
                getStringResource(R.string.what_is_the_name_of_baby)
            )
        )
        setUiState(InputBabiesInfoUiState.InputBabyName)
        setEvent(InputBabiesInfoEvent.InputText)
    }

    fun setBabyName(chatItem: ChatItem, name: String) {
        addChat(chatItem)
        nowBabyName = name
        setUiState(InputBabiesInfoUiState.InputBabyBirthDay)
        setEvent(InputBabiesInfoEvent.InputBirthDay)
    }



    fun setHaveInviteCode(haveInviteCode: Boolean) {
        val answerRes = if (haveInviteCode) R.string.answer_yes else R.string.answer_no

        val chatItem = ChatItem.UserChatItem(
            getStringResource(answerRes),
            UserChatType.HAVE_INVITE_CODE,
            canModify = true,
            isModifying = false
        )

        val nowUiState = _uiState.value
        if (nowUiState is InputBabiesInfoUiState.ModifyHaveInviteCode) {
            modifyText(chatItem, nowUiState.position)
        } else {
            addChat(chatItem)
        }
        _haveInviteCode.value = haveInviteCode
    }

    fun modifyUserChat(position: Int) {
        val chatItem = _chatList.value[position] as ChatItem.UserChatItem
        when(chatItem.userChatType){
            UserChatType.HAVE_INVITE_CODE -> {
                modifyingHaveInviteCode(position)
            }
            UserChatType.BABY_NAME -> {
                modifyingBabyInfo(position)
            }
            UserChatType.BABY_BIRTH -> {}
            else -> {

            }
        }
    }
    private fun modifyingHaveInviteCode(position: Int) {
        _chatList.value = _chatList.value.subList(0, position + 1).mapIndexed { idx, chatItem ->
            if (idx == position) {
                val modifyChatItem = chatItem as ChatItem.UserChatItem
                modifyChatItem.copy(isModifying = true)
            } else {
                chatItem
            }
        }
        setEvent(InputBabiesInfoEvent.SelectHaveInviteCode)
        setUiState(InputBabiesInfoUiState.ModifyHaveInviteCode(position))
    }

    private fun modifyingBabyInfo(position: Int) {
        _chatList.value = _chatList.value.mapIndexed { idx, chatItem ->
            if (idx == position) {
                val modifyChatItem = chatItem as ChatItem.UserChatItem
                modifyChatItem.copy(isModifying = true)
            } else {
                chatItem
            }
        }
        setEvent(InputBabiesInfoEvent.InputText)
        setUiState(InputBabiesInfoUiState.ModifyName(position))
    }

    private fun modifyText(newChatItem: ChatItem.UserChatItem, position: Int) {
        _chatList.value = _chatList.value.mapIndexed { idx, chatItem ->
            if (idx == position) {
                newChatItem
            } else {
                chatItem
            }
        }
    }

    fun modifyBabyName(userChatItem: ChatItem.UserChatItem, babyName: String, position: Int) {

        _chatList.value = _chatList.value.mapIndexed { idx, chatItem ->
            if (idx == position) {
                val nowChatItem = chatItem as ChatItem.UserChatItem

                //이미 이름과 생일이 정해진 아이의 배열에서 찾는다.
                var oldBabyIdx: Int? = null
                for (babyIdx in _babiesList.value.indices) {
                    if (_babiesList.value[babyIdx].name == nowChatItem.message) {
                        oldBabyIdx = babyIdx
                        break
                    }
                }
                //찾았다면 그 위치에 새로운 이름을 저장한다.
                if (oldBabyIdx != null) {
                    _babiesList.value = _babiesList.value.mapIndexed { babyIdx, baby ->
                        if (babyIdx == oldBabyIdx) {
                            baby.copy(name = babyName)
                        } else {
                            baby
                        }
                    }
                    userChatItem
                } else {//만약 찾지 못했다면 아직 이름만 정해진 아이이다.
                    nowBabyName = babyName
                    userChatItem
                }
            } else {
                chatItem
            }
        }
    }


    fun inputInviteCode() {

    }


    private fun addChat(chatItem: ChatItem) {
        _chatList.value += chatItem
    }

    private fun setUiState(uiState: InputBabiesInfoUiState) {
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




    companion object {
        const val KEY_USER_PROFILE = "userProfile"
    }

}
