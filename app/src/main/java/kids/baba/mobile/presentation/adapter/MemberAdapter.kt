package kids.baba.mobile.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import kids.baba.mobile.databinding.ItemMemberBinding
import kids.baba.mobile.domain.model.Member
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.model.MemberUiModel

class MemberAdapter(private val itemClick: (MemberUiModel) -> Unit) :
    ListAdapter<Member, MemberAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: ItemMemberBinding,
        private val itemClick: (MemberUiModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: MemberUiModel) {
            binding.tvMemberName.text = member.name
            binding.tvMemberRelation.text = member.introduction
                    binding.civMemberProfile.setImageResource(member.userIconUiModel.userProfileIconUiModel.iconRes)
            binding.civMemberProfile.circleBackgroundColor =
                Color.parseColor(member.userIconUiModel.iconColor)
            binding.root.setOnClickListener {
                itemClick(member)
            }
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: Member,
                newItem: Member
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position).toMemberUiModel())
    }
}