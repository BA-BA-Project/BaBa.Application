package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.databinding.FragmentAddGroupBinding
import kids.baba.mobile.presentation.view.activity.MainActivity

class AddGroupFragment : Fragment(){

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    MainActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
        }
        binding.topAppBar.tvTopTitle.text = "그룹 만들기"
        binding.bannerView.tvBannerTitle.text = "그룹을 만들어 소통을 관리해요!"
        binding.bannerView.tvBannerDesc.text = "초대된 멤버는 자신의 그룹과\n가족 그룹의 댓글만 확인할 수 있어요"
        binding.nameView.tvTitle.text = "그룹 이름"
        binding.nameView.tvDesc.text = "어떤 그룹을 만들 건가요?"
        binding.colorView.tvGroupNameTitle.text = "그룹 컬러"
        binding.permissionView.tvTitle.text = "성장앨범 업로드 권한"
        binding.permissionView.tvDesc.text = "없음"
    }

}