package kids.baba.mobile.presentation.model

sealed class ChatItem {
    data class BabaFirstChatItem(
        val message: String,
    ) : ChatItem()

    data class BabaChatItem(
        val message: String,
    ) : ChatItem()

    data class BabaChatSelectListItem(
        val iconList: List<ProfileIcon>
    ) : ChatItem()

    data class UserChatItem(
        val message: String,
        val userChatType: UserChatType,
        val canModify: Boolean,
        var isModifying: Boolean
    ) : ChatItem()

    data class UserChatWithBabyInfoItem(
        val message: String,
        val userChatType: UserChatType,
        var babyInfo: BabyInfo,
        var isModifying: Boolean
    ) : ChatItem()
}