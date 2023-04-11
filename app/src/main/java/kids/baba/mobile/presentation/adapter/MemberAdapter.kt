package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import kids.baba.mobile.databinding.ItemMemberBinding
import kids.baba.mobile.presentation.model.MemberUiModel

class MemberAdapter(private val itemClick: (MemberUiModel) -> Unit) :
    ListAdapter<MemberUiModel, MemberAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: ItemMemberBinding,
        private val itemClick: (MemberUiModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: MemberUiModel) {
            binding.tvMemberName.text = member.name
            binding.tvMemberRelation.text = member.introduction
            binding.root.setOnClickListener {
                itemClick(member)
            }
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MemberUiModel>() {
            override fun areItemsTheSame(oldItem: MemberUiModel, newItem: MemberUiModel): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: MemberUiModel,
                newItem: MemberUiModel
            ): Boolean = oldItem.name == newItem.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}