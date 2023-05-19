package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentMypageBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.adapter.MyPageGroupAdapter
import kids.baba.mobile.presentation.event.EditMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.state.MyPageUiState
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.MemberEditProfileBottomSheet
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kids.baba.mobile.presentation.viewmodel.EditMemberProfileBottomSheetViewModel
import kids.baba.mobile.presentation.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    val viewModel: MyPageViewModel by viewModels()
    private val editMemberProfileBottomSheetViewModel: EditMemberProfileBottomSheetViewModel by viewModels()
    private lateinit var babyAdapter: MemberAdapter
    private lateinit var myPageGroupAdapter: MyPageGroupAdapter

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

        collectState()
        initView()
        setBottomSheet()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGroups()
        viewModel.loadBabies()
        viewModel.getMyInfo()
    }

    private fun collectState() {
        initializeRecyclerView()
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect {
                when (it) {
                    is MyPageUiState.Idle -> {}
                    is MyPageUiState.LoadMember -> {
                        myPageGroupAdapter.submitList(it.data)
                    }

                    is MyPageUiState.LoadBabies -> {
                        babyAdapter.submitList(it.data.map { baby ->
                            baby.toMember().toPresentation()
                        })
                    }

                    is MyPageUiState.LoadMyInfo -> {
                        binding.tvMyStatusMessage.text = it.data.name
                        binding.tvMyName.text = it.data.introduction
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            editMemberProfileBottomSheetViewModel.eventFlow.collect { event ->
                when (event) {
                    is EditMemberEvent.SuccessEditMember -> {
                        viewModel.loadBabies()
                    }
                    is EditMemberEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
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
        binding.tvAddGroup.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MyPageActivity::class.java
                ).apply {
                    putExtra(INTENT_PAGE_NAME, ADD_GROUP_PAGE)
                }
            )
        }
        binding.ivSetting.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MyPageActivity::class.java
                ).apply {
                    putExtra(INTENT_PAGE_NAME, SETTING_PAGE)
                }
            )
        }
    }

    private fun initializeRecyclerView() {
        babyAdapter = MemberAdapter(
            itemClick = {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        MyPageActivity::class.java
                    ).apply {
                        putExtra(INTENT_PAGE_NAME, BABY_DETAIL_PAGE)
                        putExtra(BABY_DETAIL_INFO, it)
                    })
            })

        binding.rvKids.adapter = babyAdapter
        myPageGroupAdapter = MyPageGroupAdapter(
            showMemberInfo = { group, member ->
                val editMemberDialog = EditMemberDialog()
                val bundle = Bundle()
                bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, member)
                bundle.putString(EditMemberDialog.SELECTED_MEMBER_RELATION, group.groupName)
                editMemberDialog.arguments = bundle
                editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
            }, editGroup = { group ->
                val bundle = Bundle()
                bundle.putBoolean("family", group.family)
                bundle.putString("groupName", group.groupName)
                val bottomSheet = GroupEditBottomSheet()
                bottomSheet.arguments = bundle
                bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
            }
        )
        binding.groupContainer.adapter = myPageGroupAdapter

    }

    private fun setBottomSheet() {
        binding.ivEditKids.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = BabyEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
        binding.ivProfileEditPen.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = MemberEditProfileBottomSheet(editMemberProfileBottomSheetViewModel,
                itemClick = { profile ->
                    // 기존 -> 그냥 lifecycleScope.
                    viewLifecycleOwner.lifecycleScope.launch {
                        editMemberProfileBottomSheetViewModel.edit(profile).join()
                        viewModel.getMyInfo()
                    }
                })
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val INTENT_PAGE_NAME = "nextPage"
        const val ADD_GROUP_PAGE = "addGroupPage"
        const val SETTING_PAGE = "settingPage"
        const val BABY_DETAIL_PAGE = "babyDetailPage"
        const val BABY_DETAIL_INFO = "babyDetailInfo"
        const val INVITE_WITH_CODE_PAGE = "inviteBabyPage"
        const val ADD_BABY_PAGE = "addBabyPage"
        const val INVITE_MEMBER_PAGE = "inviteMemberPage"
    }
}