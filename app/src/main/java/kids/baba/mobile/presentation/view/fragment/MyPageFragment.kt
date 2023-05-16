package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentMypageBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.adapter.MyPageGroupAdapter
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
        viewModel.loadGroups()
        viewModel.loadBabies()
        viewModel.getMyInfo()
        collectState()
        initView()
        setBottomSheet()
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
                    putExtra("next", "addGroup")
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
        binding.ivSetting.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MyPageActivity::class.java
                ).apply {
                    putExtra("next", "setting")
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    private fun initializeRecyclerView() {
        babyAdapter = MemberAdapter {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MyPageActivity::class.java
                ).apply {
                    putExtra("next", "babyDetail")
                    putExtra("baby", it)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
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
            //            bundle.putParcelable(BabyListBottomSheet.SELECTED_BABY_KEY, viewModel.selectedBaby.value)
            val bottomSheet = BabyEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
        binding.ivProfileEditPen.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = MemberEditProfileBottomSheet(editMemberProfileBottomSheetViewModel) { profile ->
                lifecycleScope.launch{
                    editMemberProfileBottomSheetViewModel.edit(profile).join()
                    viewModel.getMyInfo()
                }
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
    }
}