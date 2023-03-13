package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemBabaSendBinding
import kids.baba.mobile.databinding.ItemBabaSendIconListBinding
import kids.baba.mobile.databinding.ItemBabaSendWithProfileBinding
import kids.baba.mobile.databinding.ItemUserSendBinding
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.ChatItem.BabaChatItem
import kids.baba.mobile.presentation.model.ChatItem.BabaChatSelectListItem
import kids.baba.mobile.presentation.model.ChatItem.BabaFirstChatItem
import kids.baba.mobile.presentation.model.ChatItem.UserChatItem
import kids.baba.mobile.presentation.model.ProfileIcon

class SignUpChatAdapter(
    private val chatEventListener: ChatEventListener,
) :
    ListAdapter<ChatItem, RecyclerView.ViewHolder>(diffUtil) {

    interface ChatEventListener {
        fun onModifyClickListener(position: Int)
        fun onIconSelectedListener(profileIcon: ProfileIcon, idx: Int, position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BabaFirstChatItem -> BABA_FIRST
            is BabaChatItem -> BABA
            is BabaChatSelectListItem -> BABA_SELECT_LIST
            is UserChatItem -> USER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BABA_FIRST -> BabaFirstChatViewHolder(parent)
            BABA -> BabaChatViewHolder(parent)
            BABA_SELECT_LIST -> BabaChatSelectItemListViewHolder(chatEventListener, parent)
            USER -> UserChatViewHolder(chatEventListener, parent)
            else -> throw Exception("unknown type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BabaFirstChatViewHolder -> holder.bind(getItem(position) as BabaFirstChatItem)
            is BabaChatSelectItemListViewHolder -> holder.bind(getItem(position) as BabaChatSelectListItem)
            is BabaChatViewHolder -> holder.bind(getItem(position) as BabaChatItem)
            is UserChatViewHolder -> holder.bind(getItem(position) as UserChatItem)
        }
    }


    class BabaFirstChatViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baba_send_with_profile, parent, false)
    ) {
        private val binding = ItemBabaSendWithProfileBinding.bind(itemView)

        fun bind(item: BabaFirstChatItem) {
            binding.tvMessage.text = item.message
        }
    }

    class BabaChatViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baba_send, parent, false)
    ) {
        private val binding = ItemBabaSendBinding.bind(itemView)

        fun bind(item: BabaChatItem) {
            binding.tvMessage.text = item.message
        }
    }

    class BabaChatSelectItemListViewHolder(
        private val chatEventListener: ChatEventListener,
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baba_send_icon_list, parent, false)
    ) {
        private val binding = ItemBabaSendIconListBinding.bind(itemView)

        private lateinit var iconList: List<ProfileIcon>
        private lateinit var selectProfileIcon: ProfileIcon

        private var profileIconAdapter: ProfileIconAdapter =
            ProfileIconAdapter(object : ProfileIconAdapter.IconClickListener {
                override fun setIconClickListener(position: Int) {
                    iconList = List(iconList.size) { idx ->
                        if (idx == position) {
                            selectProfileIcon = iconList[idx].copy(selected = true)
                            selectProfileIcon
                        } else {
                            iconList[idx].copy(selected = false)
                        }
                    }
                    itemChange(position)
                }
            })

        init {
            binding.rvProfileIcon.adapter = profileIconAdapter
        }

        fun bind(item: BabaChatSelectListItem) {
            iconList = item.iconList
            profileIconAdapter.submitList(iconList)
        }

        fun itemChange(idx: Int) {
            chatEventListener.onIconSelectedListener(selectProfileIcon, idx, layoutPosition)
            profileIconAdapter.submitList(iconList)
        }
    }

    class UserChatViewHolder(
        chatEventListener: ChatEventListener,
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_send, parent, false)
    ) {
        private val binding = ItemUserSendBinding.bind(itemView)
        private lateinit var chatItem: UserChatItem

        init {
            binding.tvModify.setOnClickListener {
                chatEventListener.onModifyClickListener(layoutPosition)
            }
        }

        fun bind(item: UserChatItem) {
            chatItem = item
            binding.userChatItem = item
        }
    }

    companion object {
        private const val BABA_FIRST = 0
        private const val BABA = 1
        private const val BABA_SELECT_LIST = 2
        private const val USER = 3


        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return if (oldItem is BabaFirstChatItem && newItem is BabaFirstChatItem) {
                    oldItem.message == newItem.message
                } else if (oldItem is BabaChatItem && newItem is BabaChatItem) {
                    oldItem.message == newItem.message
                } else if (oldItem is UserChatItem && newItem is UserChatItem) {
                    oldItem.message == newItem.message
                } else {
                    false
                }
            }

            override fun areContentsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return if (oldItem is BabaFirstChatItem && newItem is BabaFirstChatItem) {
                    oldItem.hashCode() == newItem.hashCode()
                } else if (oldItem is BabaChatItem && newItem is BabaChatItem) {
                    oldItem.hashCode() == newItem.hashCode()
                } else if (oldItem is UserChatItem && newItem is UserChatItem) {
                    oldItem.hashCode() == newItem.hashCode()
                } else {
                    false
                }
            }
        }
    }
}