package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentGreetingSelectBinding
import kids.baba.mobile.presentation.model.ChatUserType
import kids.baba.mobile.presentation.state.SignUpUiState
import kids.baba.mobile.presentation.viewmodel.SignUpViewModel


class GreetingSelectFragment : Fragment() {
    private var _binding: FragmentGreetingSelectBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: SignUpViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while(parent is NavHostFragment){
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingSelectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGreeting1.setOnClickListener {
            viewModel.addChat(ChatUserType.USER, binding.btnGreeting1.text.toString(), false)
            viewModel.setUiState(SignUpUiState.InputName)
        }

        binding.btnGreeting2.setOnClickListener {
            viewModel.addChat(ChatUserType.USER, binding.btnGreeting2.text.toString(), false)
            viewModel.setUiState(SignUpUiState.InputName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}