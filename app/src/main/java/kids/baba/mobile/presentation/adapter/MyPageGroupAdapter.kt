package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import kids.baba.mobile.databinding.ComposableGroupViewBinding
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.model.MemberUiModel

class MyPageGroupAdapter(
    private val showMemberInfo: (Group, MemberUiModel) -> Unit,
    private val editGroup: (Group) -> Unit,
    private val goToAddMemberPage: (Group) -> Unit
) :
    ListAdapter<Group, MyPageGroupAdapter.ViewHolder>(diffUtil) {
    private var ownerFamily = ""

    class ViewHolder(
        private val binding: ComposableGroupViewBinding,
        private val showMemberInfo: (Group, MemberUiModel) -> Unit,
        private val editGroup: (Group) -> Unit,
        private val goToAddMemberPage: (Group) -> Unit,
        private var ownerFamily: String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            val adapter = MemberAdapter { member -> showMemberInfo(group, member) }
            binding.groupName = group.groupName
            if (group.family) {
                binding.description = "모든 그룹과 소식을 공유할 수 있어요"
                ownerFamily = group.groupName
            } else {
                binding.description = "${ownerFamily}, ${group.groupName}의 소식만 볼 수 있어요"
            }
            if (group.members?.isEmpty() != false) {
                binding.btnAddMember.visibility = View.VISIBLE
                binding.btnAddMember.setOnClickListener {
                    goToAddMemberPage(group)
                }
            }
            binding.rvGroupMembers.adapter = adapter
            adapter.submitList(group.members?.map { it.toMemberUiModel() })
            binding.ivEditButton.setOnClickListener {
                editGroup(group)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ComposableGroupViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view,showMemberInfo, editGroup, goToAddMemberPage, ownerFamily)
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