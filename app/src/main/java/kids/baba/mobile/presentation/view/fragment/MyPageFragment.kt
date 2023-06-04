package kids.baba.mobile.presentation.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
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
        setClickEvent()
        collectState()
//        setBottomSheet()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGroups()
        viewModel.loadBabies()
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
            viewModel.eventFlow.collect {
                when (it) {
                    is MyPageEvent.Idle -> {}
                    is MyPageEvent.LoadMember -> {
                        myPageGroupAdapter.submitList(it.data)
                    }

                    is MyPageEvent.LoadBabies -> {
                        babyAdapter.submitList(it.data)
                    }

                    is MyPageEvent.LoadMyInfo -> {
                        binding.tvMyStatusMessage.text = it.data.introduction
                        binding.tvMyName.text = it.data.name
                        binding.civMyProfile.circleBackgroundColor =
                            Color.parseColor(it.data.userIconUiModel.iconColor)
                        binding.civMyProfile.setImageResource(it.data.userIconUiModel.userProfileIconUiModel.iconRes)


                    }

                    is MyPageEvent.AddGroup -> {
                        MyPageActivity.startActivity(requireContext(), pageName = ADD_GROUP_PAGE)
                    }

                    is MyPageEvent.Setting -> MyPageActivity.startActivity(
                        requireContext(),
                        pageName = SETTING_PAGE
                    )

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
                val editMemberDialog = EditMemberDialog {
                    viewModel.loadGroups()
                }
                val bundle = Bundle()
//                bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, member)
                bundle.putString(EditMemberDialog.SELECTED_MEMBER_RELATION, group.groupName)
                editMemberDialog.arguments = bundle
                editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
            }, editGroup = { group ->
                val bundle = Bundle()
                bundle.putBoolean("family", group.family)
                bundle.putString("groupName", group.groupName)
                val bottomSheet = GroupEditBottomSheet {
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
//                EncryptedPrefs.putString("babyGroupTitle", it)
//                binding.tvKidsTitle.text = it
                viewModel.refreshBabyGroupTitle(it)
            }
        )
        bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
    }

    private fun showMemberEditProfileBottomSheet() {
        val bottomSheet =
            MemberEditProfileBottomSheet(
                itemClick = {
                    viewModel.getMyInfo()
                    viewModel.loadGroups()
                }
            )
        bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
    }


    private fun setClickEvent() {
        binding.ivEditKids.setOnClickListener {
            showBabyEditBottomSheet()
        }
        binding.ivProfileEditPen.setOnClickListener {
            showMemberEditProfileBottomSheet()
        }
    }


    companion object {
        const val INTENT_PAGE_NAME = "nextPage"
        const val GROUP_NAME = "groupName"
        const val ADD_GROUP_PAGE = "addGroupPage"
        const val SETTING_PAGE = "settingPage"
        const val BABY_DETAIL_PAGE = "babyDetailPage"
        const val BABY_DETAIL_INFO = "babyDetailInfo"
        const val INVITE_WITH_CODE_PAGE = "inviteBabyPage"
        const val ADD_BABY_PAGE = "addBabyPage"
        const val INVITE_MEMBER_PAGE = "inviteMemberPage"
        const val INVITE_MEMBER_RESULT_PAGE = "inviteMemberResultPage"
        const val INVITE_CODE = "inviteCode"
    }
}