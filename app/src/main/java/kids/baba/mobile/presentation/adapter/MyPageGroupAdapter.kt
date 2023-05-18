package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import kids.baba.mobile.databinding.ComposableGroupViewBinding
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.model.MemberUiModel

class MyPageGroupAdapter(
    private val showMemberInfo: (Group, MemberUiModel) -> Unit,
    private val editGroup: (Group) -> Unit
) :
    ListAdapter<Group, MyPageGroupAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: ComposableGroupViewBinding,
        private val showMemberInfo: (Group, MemberUiModel) -> Unit,
        private val editGroup: (Group) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            val adapter = MemberAdapter { member -> showMemberInfo(group, member) }
            binding.groupName = group.groupName
            binding.description = "[${group.groupName}]그룹과 소식을 공유할 수 있어요"
            binding.rvGroupMembers.adapter = adapter
            adapter.submitList(group.members?.map { it.toMemberUiModel() })
            binding.ivEditButton.setOnClickListener {
                editGroup(group)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ComposableGroupViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, showMemberInfo, editGroup)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean =
                oldItem.groupName == newItem.groupName

            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean =
                oldItem == newItem

        }
    }
}