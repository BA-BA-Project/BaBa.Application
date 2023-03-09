package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemBabaSendBinding
import kids.baba.mobile.databinding.ItemBabaSendWithProfileBinding
import kids.baba.mobile.databinding.ItemUserSendBinding
import kids.baba.mobile.presentation.model.ChatItem

class SignUpChatAdapter() :
    ListAdapter<ChatItem, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return getItem(position).sender.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BABA_FIRST ->
                BabaFirstChatViewHolder(parent)

            BABA -> BabaChatViewHolder(parent)
            USER ->
                UserChatViewHolder(parent)

            else -> throw Exception("unknown type")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BabaFirstChatViewHolder -> holder.bind(getItem(position))
            is BabaChatViewHolder -> holder.bind(getItem(position))
            is UserChatViewHolder -> holder.bind(getItem(position))
        }
    }


    class BabaFirstChatViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_baba_send_with_profile, parent, false)
    ) {
        private val binding = ItemBabaSendWithProfileBinding.bind(itemView)

        fun bind(item: ChatItem) {
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

        fun bind(item: ChatItem) {
            binding.tvMessage.setText(item.message)
        }
    }

    class UserChatViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_send, parent, false)
    ) {
        private val binding = ItemUserSendBinding.bind(itemView)

        fun bind(item: ChatItem) {
            binding.chatItem = item
        }
    }

    companion object {
        private const val BABA_FIRST = 0
        private const val BABA = 1
        private const val USER = 2


        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return oldItem.message == newItem.message
            }

            override fun areContentsTheSame(
                oldItem: ChatItem,
                newItem: ChatItem
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}