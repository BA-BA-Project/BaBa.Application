package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentBabydetailBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.event.BabyDetailEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.mapper.toMemberUiModel
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditProfileBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
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
        setBottomSheet()
        collectState()
    }


    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyDetailEvent.SuccessBabyDetail -> {
                        familyAdapter.submitList(event.baby.familyGroup.members.map { member ->
                            member.toMemberUiModel()
                        })
                    }
                    is BabyDetailEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                    is BabyDetailEvent.BackButtonClicked -> requireActivity().finish()
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
            bundle.putParcelable(BABY_DETAIL_INFO, viewModel.baby.value)
            val bottomSheet = BabyEditProfileBottomSheet(itemClick = {
                viewModel.refresh(it)
            })
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "BabyDetailFragment"
        const val SELECTED_BABY_KEY = "SELECTED_BABY_KEY"
    }

}