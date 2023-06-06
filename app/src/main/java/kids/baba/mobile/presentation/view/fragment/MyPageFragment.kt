package kids.baba.mobile.presentation.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentMypageBinding
import kids.baba.mobile.presentation.adapter.GroupMemberAdapter
import kids.baba.mobile.presentation.adapter.MyPageGroupAdapter
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet.Companion.IS_FAMILY_KEY
import kids.baba.mobile.presentation.view.bottomsheet.MemberEditProfileBottomSheet
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kids.baba.mobile.presentation.viewmodel.MyPageViewModel

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: MyPageViewModel by viewModels()

    private lateinit var babyAdapter: GroupMemberAdapter
    private lateinit var myPageGroupAdapter: MyPageGroupAdapter

    private val args: MyPageFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectState()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBabies()
        viewModel.loadGroups()
        viewModel.getMyInfo()
        checkFromBottomNav()
    }

    private fun checkFromBottomNav() {
        if (args.fromBabyList) {
            showBabyEditBottomSheet()
        }
    }

    private fun collectState() {
        initializeRecyclerView()
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is MyPageEvent.LoadGroups -> {
                        myPageGroupAdapter.submitList(event.data)
                    }

                    is MyPageEvent.LoadBabies -> {
                        myPageGroupAdapter.setBabies(event.data.filter { babies ->
                            babies.isMyBaby
                        })
                        babyAdapter.submitList(event.data)
                    }

                    is MyPageEvent.LoadMyInfo -> {
                        binding.tvMyStatusMessage.text = event.data.introduction
                        binding.tvMyName.text = event.data.name
                        binding.civMyProfile.circleBackgroundColor =
                            Color.parseColor(event.data.userIconUiModel.iconColor)
                        binding.civMyProfile.setImageResource(event.data.userIconUiModel.userProfileIconUiModel.iconRes)

                    }

                    is MyPageEvent.AddGroup -> {
                        MyPageActivity.startActivity(requireContext(), pageName = ADD_GROUP_PAGE)
                    }

                    is MyPageEvent.Setting -> MyPageActivity.startActivity(
                        requireContext(),
                        pageName = SETTING_PAGE
                    )

                    is MyPageEvent.ShowEditMemberBottomSheet -> showMemberEditProfileBottomSheet(event.memberUiModel)

                    is MyPageEvent.ShowEditBabyGroupBottomSheet -> showBabyEditBottomSheet()

                    else -> {}
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initializeRecyclerView() {
        babyAdapter = GroupMemberAdapter(
            itemClick = {
                MyPageActivity.startActivityWithMember(
                    requireContext(),
                    pageName = BABY_DETAIL_PAGE,
                    argumentName = BABY_DETAIL_INFO,
                    groupMember = it as BabyUiModel
                )
            })

        binding.rvKids.adapter = babyAdapter
        myPageGroupAdapter = MyPageGroupAdapter(
            showMemberInfo = { group, member ->
                val editMemberDialog = EditMemberDialog(
                    itemClick = { viewModel.loadGroups() }
                )
                val bundle = Bundle()
                bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, member)
                bundle.putString(EditMemberDialog.SELECTED_GROUP_KEY, group.groupName)
                editMemberDialog.arguments = bundle
                editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
            },
            showBabyInfo = {
                MyPageActivity.startActivityWithMember(
                    requireContext(),
                    pageName = BABY_DETAIL_PAGE,
                    argumentName = BABY_DETAIL_INFO,
                    groupMember = it
                )
            }, editGroup = { group ->
                val bundle = Bundle()
                bundle.putBoolean(IS_FAMILY_KEY, group.family)
                bundle.putString(GROUP_NAME, group.groupName)
                bundle.putString(GROUP_COLOR, group.members[0].iconColor)

                val bottomSheet = GroupEditBottomSheet {
                    viewModel.loadBabies()
                    viewModel.loadGroups()
                }
                bottomSheet.arguments = bundle
                bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
            }, goToAddMemberPage = { group ->
                MyPageActivity.startActivityWithGroupName(
                    requireContext(),
                    INVITE_MEMBER_PAGE,
                    GROUP_NAME,
                    group.groupName
                )
            }
        )
        binding.groupContainer.adapter = myPageGroupAdapter

    }

    private fun showBabyEditBottomSheet() {
        val bottomSheet = BabyEditBottomSheet(
            itemClick =
            {
                viewModel.refreshBabyGroupTitle(it)
            }
        )
        bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
    }

    private fun showMemberEditProfileBottomSheet(memberUiModel: MemberUiModel) {
        val bottomSheet =
            MemberEditProfileBottomSheet(
                itemClick = {
                    viewModel.getMyInfo()
                    viewModel.loadGroups()
                }
            )
        val bundle = bundleOf(MEMBER_UI_MODEL to memberUiModel)
        bottomSheet.arguments = bundle
        bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
    }


    companion object {
        const val INTENT_PAGE_NAME = "nextPage"
        const val GROUP_NAME = "groupName"
        const val GROUP_COLOR = "groupColor"
        const val ADD_GROUP_PAGE = "addGroupPage"
        const val SETTING_PAGE = "settingPage"
        const val BABY_DETAIL_PAGE = "babyDetailPage"
        const val BABY_DETAIL_INFO = "babyDetailInfo"
        const val INVITE_WITH_CODE_PAGE = "inviteBabyPage"
        const val ADD_BABY_PAGE = "addBabyPage"
        const val INVITE_MEMBER_PAGE = "inviteMemberPage"
        const val INVITE_MEMBER_RESULT_PAGE = "inviteMemberResultPage"
        const val INVITE_CODE = "inviteCode"
        const val MEMBER_UI_MODEL = "memberUiModel"
    }
}