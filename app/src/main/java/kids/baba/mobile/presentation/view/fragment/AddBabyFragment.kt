package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentAddbabyBinding

@AndroidEntryPoint
class AddBabyFragment: Fragment() {

    private var _binding: FragmentAddbabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddbabyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddBaby.setOnClickListener {
            val fragment = AddCompleteFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.topAppBar.tvTopTitle.text = "아이 추가하기"
        binding.bannerView.tvBannerTitle.text = "내 아이를 추가해봐요"
        binding.bannerView.tvBannerDesc.text = "직접 성장앨범을 기록할 아이를 만들어요."
        binding.nameView.tvInputTitle.text = "아이 이름"
        binding.relationView.tvTitle.text = "나와 아이의 관계"
        binding.relationView.tvDesc.text = "내가 추가한 아이들와 나의 관계입니다."
        binding.birthView.tvInputTitle.text = "아이 생일 혹은 출산 예정일"
    }
}