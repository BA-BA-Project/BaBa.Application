package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.databinding.FragmentAskBinding
import kids.baba.mobile.databinding.FragmentCreatorBinding

class FragmentCreator : Fragment() {
    private var _binding: FragmentCreatorBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.tvTopTitle.text = "서비스 제작자"
        binding.design.tvTitle.text = "기획 및 디자인"
        binding.design.tvDesc.text = "박재희"
        binding.android.tvTitle.text = "안드로이드 개발"
        binding.android.tvDesc.text = "심지훈\n이윤호\n이호성"
        binding.backend.tvTitle.text = "백엔드 개발"
        binding.backend.tvDesc.text = "김준형\n박성우"
        binding.helper.tvTitle.text = "도운주신 분"
        binding.helper.tvDesc.text = "서종환"
    }
}