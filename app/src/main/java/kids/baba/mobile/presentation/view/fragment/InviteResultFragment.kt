package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInviteResultBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

@AndroidEntryPoint
class InviteResultFragment : Fragment() {

    private var _binding: FragmentInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnComplete.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MainActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
        }
        binding.addTop.tvTopTitle.text = "아이 추가하기"
        binding.bannerView.tvBannerTitle.text = "아이가 추가되었어요!"
        binding.bannerView.tvBannerDesc.text ="아이의 소식을 받고 소통해봐요 :)"
        binding.nameView.tvTitle.text = " 아이 이름"
        binding.nameView.tvDesc.text = "티티"
        binding.groupView.tvTitle.text = "나의 소속 그룹"
        binding.groupView.tvDesc.text = "친구"
        binding.relationView.tvTitle.text = "나와 아이와 관계"
        binding.relationView.tvDesc.text = "이모"
        binding.permissionView.tvTitle.text ="성장앨범 업로드 권한"
        binding.permissionView.tvDesc.text = "없음"
    }
}
