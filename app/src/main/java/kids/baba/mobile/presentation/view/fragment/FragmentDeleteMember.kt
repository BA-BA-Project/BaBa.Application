package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentDeleteUserBinding
import kids.baba.mobile.databinding.FragmentServiceInfoBinding

@AndroidEntryPoint
class FragmentDeleteMember: Fragment() {

    private var _binding: FragmentDeleteUserBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.tvTopTitle.text = "계정 삭제"
        binding.bannerView.tvBannerTitle.text = "계정을 삭제하려는 이유가\n 무엇인가요?"
        binding.bannerView.tvBannerDesc.text = "서비스 개선을 위해 선택해주세요."
        binding.forDataDelete.tvContent.text = "데이터 삭제를 위해"
        binding.uncomfortable.tvContent.text = "불편한 사용성으로 인해"
        binding.frequentErrors.tvContent.text = "잦은 오류로 인해"
        binding.notUse.tvContent.text = "낮은 사용 빈도로 인해"
        binding.etc.tvContent.text = "기타"
    }

}