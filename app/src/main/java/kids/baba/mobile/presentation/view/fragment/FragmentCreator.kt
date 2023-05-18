package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentCreatorBinding

@AndroidEntryPoint
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = "서비스 제작자"
        binding.design = "기획 및 디자인"
        binding.designer = "박재희"
        binding.android = "안드로이드 개발"
        binding.androidDev = "심지훈\n이윤호\n이호성"
        binding.backEnd = "백엔드 개발"
        binding.backEndDev = "김준형\n박성우"
        binding.helper = "도움주신 분"
        binding.helperName = "서종환"
        binding.topAppBar.ivBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}