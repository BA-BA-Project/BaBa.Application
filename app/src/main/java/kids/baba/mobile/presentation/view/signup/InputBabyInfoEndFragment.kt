package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentInputBabyInfoEndBinding
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

class InputBabyInfoEndFragment : Fragment() {

    private var _binding: FragmentInputBabyInfoEndBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: InputBabiesInfoViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    private val activityViewModel: IntroViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBabyInfoEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInputBabyInfoEnd.setOnClickListener {
            viewModel.signUp()
            activityViewModel.isSignUpSuccess()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}