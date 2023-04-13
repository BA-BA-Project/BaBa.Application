package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.databinding.FragmentInviteResultBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

class InviteMemberFragment : Fragment() {

    private var _binding: FragmentInviteMemberBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInvite.setOnClickListener {
            val fragment = InviteMemberResultFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.topAppBar.tvTopTitle.text = "멤버 초대"
        binding.bannerView.tvBannerTitle.text = "아이와 나의 소식을 공유할\n멤버를 초대해요"
        binding.bannerView.tvBannerDesc.text = "직접 생성한 아이의 성장앨범과\n자신의 일기를 공유할 수 있어요."
        binding.inputGroupView.tvTitle.text = "초대 멤버의 소속 그룹"
        binding.inputGroupView.tvDesc.text = "초대받을 멤버가 어떤 그룹인가요?"
        binding.inputRelationView.tvTitle.text = "초대 멤버와 아이의 관계"
        binding.inputRelationView.tvDesc.text = "초대받을 멤버의 별명으로 초대 멤버와\n 그룹에게 공개됩니다."
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteMemberBinding.inflate(inflater, container, false)
        return binding.root
    }
}