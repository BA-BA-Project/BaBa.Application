package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.databinding.FragmentMemberInviteResultBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

class InviteMemberResultFragment : Fragment() {

    private var _binding: FragmentMemberInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
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
        binding.topAppBar.tvTopTitle.text = "멤버 초대"
        binding.bannerView.tvBannerTitle.text = "멤버 초대를 완료했어요!"
        binding.bannerView.tvBannerDesc.text = "멤버들과 소통하며 즐거운 시간 보내요 :)"
        binding.groupView.tvTitle.text = "초대 멤버의 소속 그룹"
        binding.groupView.tvDesc.text = "친구"
        binding.relationView.tvTitle.text = "초대 멤버와 아이의 관계"
        binding.relationView.tvDesc.text = "이모"
        binding.permissionView.tvTitle.text = "성장앨범 업로드 권한"
        binding.permissionView.tvDesc.text = "없음"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInviteResultBinding.inflate(inflater, container, false)
        return binding.root
    }
}