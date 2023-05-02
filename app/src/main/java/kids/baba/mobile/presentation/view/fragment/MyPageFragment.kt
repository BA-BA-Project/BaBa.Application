package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentMypageBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
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
    private lateinit var babyAdapter: MemberAdapter
    private lateinit var familyAdapter: MemberAdapter
    private lateinit var motherFamilyAdapter: MemberAdapter
    private lateinit var fatherFamilyAdapter: MemberAdapter
    private lateinit var friendsAdapter: MemberAdapter
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
        setBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.viewmodel = viewModel
        binding.tvAddGroup.setOnClickListener {
            val fragment = AddGroupFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.ivSetting.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MyPageActivity::class.java
                ).putExtra("next", "setting")
            )
        }
        initializeRecyclerView()
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
                })
        }
        familyAdapter = MemberAdapter {
            val editMemberDialog = EditMemberDialog()
            val bundle = Bundle()
            bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, it)
            editMemberDialog.arguments = bundle
            editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
        }

        motherFamilyAdapter = MemberAdapter {
            val editMemberDialog = EditMemberDialog()
            val bundle = Bundle()
            bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, it)
            editMemberDialog.arguments = bundle
            editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
        }

        fatherFamilyAdapter = MemberAdapter {
            val editMemberDialog = EditMemberDialog()
            val bundle = Bundle()
            bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, it)
            editMemberDialog.arguments = bundle
            editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
        }

        friendsAdapter = MemberAdapter {
            val editMemberDialog = EditMemberDialog()
            val bundle = Bundle()
            bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, it)
            editMemberDialog.arguments = bundle
            editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
        }
        binding.rvKids.adapter = babyAdapter
        binding.rvKids.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.familyView.rvGroupMembers.adapter = familyAdapter
        binding.familyView.rvGroupMembers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.motherFamilyView.rvGroupMembers.adapter = motherFamilyAdapter
        binding.motherFamilyView.rvGroupMembers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.fatherFamilyView.rvGroupMembers.adapter = fatherFamilyAdapter
        binding.fatherFamilyView.rvGroupMembers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvFriends.adapter = friendsAdapter
        binding.rvFriends.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        babyAdapter.submitList(getDummy())
        familyAdapter.submitList(getDummy())
        motherFamilyAdapter.submitList(getDummy())
        fatherFamilyAdapter.submitList(getDummy())
        friendsAdapter.submitList(getDummy())
    }

    private fun getDummy(): List<MemberUiModel> {
        val list = mutableListOf<MemberUiModel>()
        repeat(5) {
            list.add(
                MemberUiModel(
                    "이윤호",
                    "형",
                    UserIconUiModel(UserProfileIconUiModel.PROFILE_BABY_1, "red")
                )
            )
        }
        return list
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
            val bottomSheet = MemberEditProfileBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditBottomSheet.TAG)
        }
        binding.familyView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
        binding.motherFamilyView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
        binding.fatherFamilyView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
        binding.ivEditFriends.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
    }
}