package kids.baba.mobile.presentation.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentBabydetailBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.state.BabyDetailUiState
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditProfileBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
import kids.baba.mobile.presentation.viewmodel.BabyDetailViewModel

@AndroidEntryPoint
class BabyDetailFragment : Fragment() {

    private lateinit var familyAdapter: MemberAdapter
    private lateinit var myGroupAdapter: MemberAdapter
    private var _binding: FragmentBabydetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyDetailViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initView()
        collectState()
        setBottomSheet()
    }

    private fun initView() {
        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }
        viewModel.baby.value?.let {
            viewModel.uiModel.value.babyName = it.name
            binding.civMyProfile.setImageResource(it.userIconUiModel.userProfileIconUiModel.iconRes)
            binding.civMyProfile.circleBackgroundColor =
                Color.parseColor(it.userIconUiModel.iconColor)
            viewModel.load(it.memberId)
        }
    }

    private fun collectState() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it) {
                    is BabyDetailUiState.Idle -> {}
                    is BabyDetailUiState.Success -> {
                        binding.familyView.tvGroupTitle.text = it.data.familyGroup.groupName
                        binding.tvMyStatusMessage.text = it.data.birthday
                        binding.myGroupView.tvGroupTitle.text =
                            if (it.data.myGroup == null) "없음" else it.data.myGroup.groupName
                        binding.myGroupView.tvGroupDesc.text =
                            if (it.data.myGroup == null) "없음" else "${it.data.familyGroup.groupName}, ${it.data.myGroup.groupName}의 소식만 볼 수 있어요"
                        familyAdapter.submitList(it.data.familyGroup.members.map { member -> member.toMemberUiModel() })
                        binding.btnDeleteBaby.setOnClickListener {
                            Log.e("delete", "${viewModel.baby.value?.memberId}")
                            viewModel.delete(viewModel.baby.value?.memberId ?: "")
                            findNavController().navigateUp()
                        }
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

    private fun setBottomSheet() {
        binding.ivProfileEditPen.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(BabyEditProfileBottomSheet.SELECTED_BABY_KEY, viewModel.baby.value)
            val bottomSheet = BabyEditProfileBottomSheet {
                binding.tvMyName.text = it
            }
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditProfileBottomSheet.TAG)
        }
        binding.myGroupView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet {}
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
        binding.familyView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet {}
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBabydetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun initializeRecyclerView() {
        familyAdapter = MemberAdapter {

        }
        myGroupAdapter = MemberAdapter {

        }
        binding.familyView.rvGroupMembers.adapter = familyAdapter
        binding.familyView.rvGroupMembers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.myGroupView.rvGroupMembers.adapter = myGroupAdapter
        binding.myGroupView.rvGroupMembers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    companion object {
        const val TAG = "BabyDetailFragment"
        const val SELECTED_BABY_KEY = "SELECTED_BABY_KEY"
    }

}