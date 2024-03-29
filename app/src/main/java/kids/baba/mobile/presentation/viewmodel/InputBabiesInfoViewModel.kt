package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.model.getThrowableOrNull
import kids.baba.mobile.domain.usecase.GetBabiesInfoByInviteCodeUseCase
import kids.baba.mobile.domain.usecase.SignUpUseCase
import kids.baba.mobile.presentation.event.InputBabiesInfoEvent
import kids.baba.mobile.presentation.model.BabyInfo
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InputBabiesInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val signUpUseCase: SignUpUseCase,
    private val getBabiesInfoByInviteCodeUseCase: GetBabiesInfoByInviteCodeUseCase
) : ViewModel() {

    private val userProfile = savedStateHandle.get<UserProfile>(KEY_USER_PROFILE)
    private val signToken = savedStateHandle[KEY_SIGN_TOKEN] ?: ""

    private val _uiState: MutableStateFlow<InputBabiesInfoUiState> =
        MutableStateFlow(InputBabiesInfoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _lastUiState: MutableStateFlow<InputBabiesInfoUiState> =
        MutableStateFlow(InputBabiesInfoUiState.Loading)

    private val _eventFlow = MutableEventFlow<InputBabiesInfoEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _chatList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatList = _chatList.asStateFlow().map { it.toList() }

    private val _babiesList = MutableStateFlow<List<BabyInfo>>(emptyList())
    private val babiesList = _babiesList.asStateFlow()

    private var relation = ""

    private var inviteCode = ""

    private var inputMoreBaby = true

    init {
        checkInviteCode()
    }

    private fun initData() {
        relation = ""
        inviteCode = ""
        inputMoreBaby = true
        _babiesList.value = emptyList()
    }

    private fun checkInviteCode() {
        addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.check_have_invite_code)))
        setUiState(InputBabiesInfoUiState.CheckInviteCode)
    }

    private fun inputBabiesInfo() {
        inputMoreBaby = true

        addChat(
            ChatItem.BabaFirstChatItem(
                getStringResource(R.string.need_to_baby_info)
            )
        )
        createNewBaby()
    }

    private fun createNewBaby() {
        val babyInfo = BabyInfo()
        val message = getStringResource(R.string.what_is_the_name_of_baby)
        addChat(
            if (_babiesList.value.isEmpty()) {
                ChatItem.BabaChatItem(message)
            } else {
                ChatItem.BabaFirstChatItem(message)
            }
        )

        setUiState(InputBabiesInfoUiState.InputBabyName(babyInfo))
    }

    fun setBabyName(chatItem: ChatItem.UserChatWithBabyInfoItem) {
        addChat(chatItem)
        addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.input_baby_birthday)))
        setUiState(InputBabiesInfoUiState.InputBabyBirthDay(chatItem.babyInfo))
    }

    fun setBabyBirthday(chatItem: ChatItem.UserChatWithBabyInfoItem) {
        addChat(chatItem)
        addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.input_more_baby)))
        if (checkBabyInfo(chatItem.babyInfo)) {
            setUiState(InputBabiesInfoUiState.CheckMoreBaby)
        }
    }

    private fun checkBabyInfo(babyInfo: BabyInfo): Boolean {
        val name = babyInfo.babyName
        val birthday = babyInfo.birthday

        return if (name.isNullOrEmpty().not() && birthday != null) {
            _babiesList.value += babyInfo
            true
        } else {
            addChat(
                ChatItem.BabaFirstChatItem(getStringResource(R.string.baby_info_error))
            )
            createNewBaby()
            false
        }
    }


    fun setHaveInviteCode(haveInviteCode: Boolean) {

        val answerRes = if (haveInviteCode) {
            R.string.answer_yes
        } else {
            R.string.answer_no
        }

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

        if (haveInviteCode) {
            inputInviteCode()
        } else {
            inputBabiesInfo()
        }
    }

    fun modifyUserChat(position: Int) {
        val chatItem = _chatList.value[position]

        if (chatItem is ChatItem.UserChatItem) {
            when (chatItem.userChatType) {
                UserChatType.HAVE_INVITE_CODE -> {
                    modifyingHaveInviteCode(position)
                }

                else -> {}
            }
        } else if (chatItem is ChatItem.UserChatWithBabyInfoItem) {
            when (chatItem.userChatType) {
                UserChatType.BABY_NAME -> {
                    modifyingBabyInfo(position)
                    setUiState(InputBabiesInfoUiState.ModifyName(chatItem.babyInfo, position))
                }

                UserChatType.BABY_BIRTH -> {
                    modifyingBabyInfo(position)
                    setUiState(InputBabiesInfoUiState.ModifyBirthday(chatItem.babyInfo, position))
                }

                else -> {}
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
        initData()
        setUiState(InputBabiesInfoUiState.ModifyHaveInviteCode(position))
    }

    private fun modifyingBabyInfo(position: Int) {
        _chatList.value = _chatList.value.mapIndexed { idx, chatItem ->
            if (idx == position) {
                val modifyChatItem = chatItem as ChatItem.UserChatWithBabyInfoItem
                modifyChatItem.copy(isModifying = true)
            } else {
                chatItem
            }
        }
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

    fun modifyBabyInfo(
        newChatItem: ChatItem.UserChatWithBabyInfoItem,
        position: Int
    ) {
        val newBabyInfo = newChatItem.babyInfo
        _chatList.value = _chatList.value.mapIndexed { idx, chatItem ->
            if (idx == position) {
                val nowChatItem = chatItem as ChatItem.UserChatWithBabyInfoItem

                val oldBabyInfo = nowChatItem.babyInfo
                _babiesList.value = _babiesList.value.map {
                    if (it == oldBabyInfo) {
                        newBabyInfo
                    } else {
                        it
                    }
                }
                newChatItem
            } else {
                chatItem
            }
        }
        if (newBabyInfo.birthday == null) {
            setUiState(InputBabiesInfoUiState.InputBabyBirthDay(newBabyInfo))
        } else {
            setUiState(_lastUiState.value)
        }
    }


    fun inputMoreBaby(check: Boolean) {
        val message = if (check) R.string.answer_yes else R.string.answer_no
        addChat(
            ChatItem.UserChatItem(
                getStringResource(message),
                UserChatType.INPUT_TEXT,
                canModify = false,
                isModifying = false
            )
        )
        if (check) {
            createNewBaby()
        } else {
            addChat(ChatItem.BabaFirstChatItem(getStringResource(R.string.input_relation)))
            setUiState(InputBabiesInfoUiState.InputRelation)
        }
    }

    fun setRelation(relation: String) {
        this.relation = relation
        addChat(
            ChatItem.UserChatItem(
                relation,
                UserChatType.RELATION,
                canModify = false,
                isModifying = false
            )
        )
        addChat(
            ChatItem.BabaFirstChatItem(
                getStringResource(R.string.input_end_babies_info)
            )
        )
        setUiState(InputBabiesInfoUiState.InputEndBabiesInfo)
    }

    private fun inputInviteCode() {
        addChat(
            ChatItem.BabaFirstChatItem(
                getStringResource(R.string.please_input_invite_code)
            )
        )
        setUiState(InputBabiesInfoUiState.InputInviteCode)
    }

    fun setInviteCode(inviteCode: String) {
        this.inviteCode = inviteCode
        addChat(
            ChatItem.UserChatItem(
                inviteCode,
                UserChatType.INPUT_TEXT,
                canModify = false,
                isModifying = false
            )
        )
        viewModelScope.launch {
            when(val result = getBabiesInfoByInviteCodeUseCase(inviteCode)){
                is Result.Success -> {
                    val sj = StringJoiner(", ")
                    val babiesList = result.data.babies
                    val relationName = result.data.relationName

                    babiesList.forEach { baby ->
                        sj.add(baby.babyName)
                    }
                    val babies = sj.toString()
                    addChat(
                        ChatItem.BabaFirstChatItem(
                            String.format(
                                getStringResource(R.string.babies_info_by_invite_code),
                                babies,
                                relationName
                            )
                        )
                    )
                    addChat(
                        ChatItem.BabaFirstChatItem(
                            getStringResource(R.string.input_end_babies_info)
                        )
                    )
                    setUiState(InputBabiesInfoUiState.GetBabiesInfoByInviteCode)
                }
                is Result.NetworkError -> {
                    addChat(
                        ChatItem.BabaFirstChatItem(
                            getStringResource(R.string.baba_network_failed)
                        )
                    )
                }
                else -> {
                    addChat(
                        ChatItem.BabaFirstChatItem(
                            getStringResource(R.string.invite_code_error)
                        )
                    )
                }
            }

        }
    }

    fun signUpWithBabiesInfo() {
        if (userProfile != null) {
            viewModelScope.launch {
                when (
                    val result = signUpUseCase.signUpWithBabiesInfo(
                        signToken,
                        SignUpRequestWithBabiesInfo(
                            userProfile.name,
                            userProfile.iconName,
                            relation,
                            babiesList.value
                        )
                    )) {
                    is Result.Success -> {
                        setUiState(InputBabiesInfoUiState.SignUpSuccess(userProfile.name))
                    }
                    else -> {
                        val throwable = result.getThrowableOrNull()
                        if(throwable != null){
                            setUiState(InputBabiesInfoUiState.SignUpFailed(throwable))
                        }
                    }
                }
            }
        }
    }

    fun signUpWithInviteCode() {
        if (userProfile != null) {
            viewModelScope.launch {
                when (
                    val result = signUpUseCase.signUpWithInviteCode(
                        signToken, SignUpRequestWithInviteCode(
                            inviteCode,
                            userProfile.name,
                            userProfile.iconName
                        )
                    )) {
                    is Result.Success -> {
                        setUiState(InputBabiesInfoUiState.SignUpSuccess(userProfile.name))
                    }
                    else -> {
                        val throwable = result.getThrowableOrNull()
                        if(throwable != null){
                            setUiState(InputBabiesInfoUiState.SignUpFailed(throwable))
                        }
                    }
                }
            }
        }
    }


    private fun addChat(chatItem: ChatItem) {
        _chatList.value += chatItem
    }

    private fun setUiState(uiState: InputBabiesInfoUiState) {
        _lastUiState.value = _uiState.value
        _uiState.value = uiState
    }

    private fun getStringResource(resourceId: Int): String {
        return resources.getString(resourceId)
    }

    fun setEvent(event: InputBabiesInfoEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }


    companion object {
        const val KEY_USER_PROFILE = "userProfile"
        const val KEY_SIGN_TOKEN = "signToken"
    }

}
