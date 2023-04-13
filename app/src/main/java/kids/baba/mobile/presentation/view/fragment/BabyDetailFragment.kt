package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kids.baba.mobile.databinding.FragmentBabydetailBinding
import kids.baba.mobile.presentation.adapter.MemberAdapter
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import kids.baba.mobile.presentation.view.bottomsheet.BabyEditProfileBottomSheet
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet

class BabyDetailFragment : Fragment() {

    private lateinit var familyAdapter: MemberAdapter
    private lateinit var myGroupAdapter: MemberAdapter

    private var _binding: FragmentBabydetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        Log.e("bundle", "${this.arguments?.getParcelable<MemberUiModel>(SELECTED_BABY_KEY)}")
        setBottomSheet()
        binding.familyView.tvGroupTitle.text = "가족 그룹 이름"
        binding.familyView.tvGroupDesc.text = "모든 그룹과 소식을 공유할 수 있어요"
        binding.myGroupView.tvGroupTitle.text = "내가 속한 그룹 이름"
        binding.myGroupView.tvGroupDesc.text = "[가족 그룹], [내가 속한 그룹]의 소식만 볼 수 있어요"
    }

    private fun setBottomSheet() {
        binding.ivProfileEditPen.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = BabyEditProfileBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, BabyEditProfileBottomSheet.TAG)
        }
        binding.myGroupView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
        }
        binding.familyView.ivEditButton.setOnClickListener {
            val bundle = Bundle()
            val bottomSheet = GroupEditBottomSheet()
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

        familyAdapter.submitList(getDummy())
        myGroupAdapter.submitList(getDummy())
    }

    private fun getDummy(): List<MemberUiModel> {
        val list = mutableListOf<MemberUiModel>()
        repeat(3) {
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

    companion object {
        const val TAG = "BabyDetailFragment"
        const val SELECTED_BABY_KEY = "SELECTED_BABY_KEY"
    }
}