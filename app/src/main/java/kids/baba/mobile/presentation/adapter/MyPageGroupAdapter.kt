package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ComposableGroupViewBinding
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.GroupMember
import kids.baba.mobile.presentation.model.GroupUiModel
import kids.baba.mobile.presentation.model.MemberUiModel

class MyPageGroupAdapter(
    private val showMemberInfo: (GroupUiModel, GroupMember) -> Unit,
    private val showBabyInfo: (BabyUiModel) -> Unit,
    private val editGroup: (GroupUiModel) -> Unit,
    private val goToAddMemberPage: (GroupUiModel) -> Unit
) :
    ListAdapter<GroupUiModel, MyPageGroupAdapter.ViewHolder>(diffUtil) {
    private var babies: List<BabyUiModel> = emptyList()


    fun setBabies(babies: List<BabyUiModel>) {
        this.babies = babies
    }

    class ViewHolder(
        private val binding: ComposableGroupViewBinding,
        private val showMemberInfo: (GroupUiModel, GroupMember) -> Unit,
        private val showBabyInfo: (BabyUiModel) -> Unit,
        private val editGroup: (GroupUiModel) -> Unit,
        private val goToAddMemberPage: (GroupUiModel) -> Unit,
        private var babies: List<BabyUiModel>
    ) : RecyclerView.ViewHolder(binding.root) {

        lateinit var group: GroupUiModel
        lateinit var adapter: GroupMemberAdapter

        init {
            binding.btnAddMember.setOnClickListener {
                goToAddMemberPage(group)
            }
        }

        fun bind(group: GroupUiModel) {
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
                adapter.submitList(babies + group.members.map { it.toMemberUiModel() })
            } else {
                binding.description = String.format(
                    binding.root.context.getString(R.string.my_group_description),
                    group.ownerFamily,
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
            babies
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GroupUiModel>() {
            override fun areItemsTheSame(oldItem: GroupUiModel, newItem: GroupUiModel): Boolean =
                oldItem.groupName == newItem.groupName

            override fun areContentsTheSame(oldItem: GroupUiModel, newItem: GroupUiModel): Boolean =
                oldItem == newItem

        }
    }
}