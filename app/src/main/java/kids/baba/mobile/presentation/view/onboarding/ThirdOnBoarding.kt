package kids.baba.mobile.presentation.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kids.baba.mobile.databinding.FragmentThirdOnBoardingBinding
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

class ThirdOnBoarding : Fragment() {

    private var _binding: FragmentThirdOnBoardingBinding? = null
    private val viewModel: IntroViewModel by activityViewModels()
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
    }

    private fun setViewModel() {
        binding.viewModel = viewModel
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}