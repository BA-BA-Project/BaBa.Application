package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemGroupBabyBinding
import kids.baba.mobile.databinding.ItemGroupMemberBinding
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.GroupMember
import kids.baba.mobile.presentation.model.MemberUiModel

class GroupMemberAdapter(private val itemClick: (GroupMember) -> Unit) :
    ListAdapter<GroupMember, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BabyUiModel -> BABY
            else -> MEMBER
        }
    }

    class MemberViewHolder(
        parent: ViewGroup,
        private val itemClick: (GroupMember) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_group_member, parent, false)
    ) {
        private val binding = ItemGroupMemberBinding.bind(itemView)
        private lateinit var member: MemberUiModel

        init {
            binding.root.setOnClickListener {
                itemClick(member)
            }
        }

        fun bind(member: MemberUiModel) {
            this.member = member
            binding.member = member
        }
    }

    class BabyViewHolder(
        parent: ViewGroup,
        private val itemClick: (GroupMember) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_group_baby, parent, false)
    ) {
        private val binding = ItemGroupBabyBinding.bind(itemView)

        lateinit var baby: BabyUiModel

        init {
            binding.root.setOnClickListener {
                itemClick(baby)
            }
        }

        fun bind(baby: BabyUiModel) {
            this.baby = baby
            binding.baby = baby
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BABY -> BabyViewHolder(parent, itemClick)
            else -> MemberViewHolder(parent, itemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BabyViewHolder -> holder.bind(getItem(position) as BabyUiModel)
            is MemberViewHolder -> holder.bind(getItem(position) as MemberUiModel)
        }
    }

    companion object {
        private const val BABY = 0
        private const val MEMBER = 1

        val diffUtil = object : DiffUtil.ItemCallback<GroupMember>() {
            override fun areItemsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean =
                if (oldItem is MemberUiModel && newItem is MemberUiModel) {
                    oldItem.name == newItem.name
                } else if (oldItem is BabyUiModel && newItem is BabyUiModel) {
                    oldItem.name == newItem.name
                } else {
                    false
                }

            override fun areContentsTheSame(oldItem: GroupMember, newItem: GroupMember): Boolean =
                if (oldItem is MemberUiModel && newItem is MemberUiModel) {
                    oldItem == newItem
                } else if (oldItem is BabyUiModel && newItem is BabyUiModel) {
                    oldItem == newItem
                } else {
                    false
                }

        }
    }
}