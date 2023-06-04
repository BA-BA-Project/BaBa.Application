package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ComposableGroupViewBinding
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.GroupMember
import kids.baba.mobile.presentation.model.MemberUiModel

class MyPageGroupAdapter(
    private val showMemberInfo: (Group, GroupMember) -> Unit,
    private val showBabyInfo: (BabyUiModel) -> Unit,
    private val editGroup: (Group) -> Unit,
    private val goToAddMemberPage: (Group) -> Unit
) :
    ListAdapter<Group, MyPageGroupAdapter.ViewHolder>(diffUtil) {
    private var ownerFamily = ""
    private var babies: List<BabyUiModel> = emptyList()

    fun setBabies(babies: List<BabyUiModel>) {
        this.babies = babies
    }

    class ViewHolder(
        private val binding: ComposableGroupViewBinding,
        private val showMemberInfo: (Group, GroupMember) -> Unit,
        private val showBabyInfo: (BabyUiModel) -> Unit,
        private val editGroup: (Group) -> Unit,
        private val goToAddMemberPage: (Group) -> Unit,
        private var ownerFamily: String,
        private var babies: List<BabyUiModel>
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var group: Group
        lateinit var adapter: GroupMemberAdapter

        init {
            binding.btnAddMember.setOnClickListener {
                goToAddMemberPage(group)
            }
        }

        fun bind(group: Group) {
            this.group = group
            adapter = GroupMemberAdapter { groupMember ->
                when (groupMember) {
                    is BabyUiModel -> showBabyInfo(groupMember)
                    is MemberUiModel -> showMemberInfo(group, groupMember)
                }
            }

            binding.groupName = group.groupName
            binding.rvGroupMembers.adapter = adapter
            if (group.family) {
                binding.description =
                    binding.root.context.getString(R.string.family_group_description)
                ownerFamily = group.groupName
                adapter.submitList(babies + group.members.map { it.toMemberUiModel() })
            } else {
                binding.description = String.format(
                    binding.root.context.getString(R.string.my_group_description),
                    ownerFamily,
                    group.groupName
                )
                adapter.submitList(group.members.map { it.toMemberUiModel() })
            }
            if (group.members.isEmpty()) {
                binding.btnAddMember.visibility = View.VISIBLE
            }
            binding.ivEditButton.setOnClickListener {
                editGroup(group)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ComposableGroupViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            view,
            showMemberInfo,
            showBabyInfo,
            editGroup,
            goToAddMemberPage,
            ownerFamily,
            babies
        )
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