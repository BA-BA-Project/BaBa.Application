package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentGreetingSelectBinding
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.state.CreateProfileUiState
import kids.baba.mobile.presentation.viewmodel.CreateProfileViewModel


class GreetingSelectFragment : Fragment() {
    private var _binding: FragmentGreetingSelectBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: CreateProfileViewModel by viewModels(
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
            viewModel.addChat(ChatItem.UserChatItem(
                binding.btnGreeting1.text.toString(),
                UserChatType.GREETING,
                canModify = false,
                isModifying = false
            ))
            viewModel.setUiState(CreateProfileUiState.InputName)
        }

        binding.btnGreeting2.setOnClickListener {
            viewModel.addChat(ChatItem.UserChatItem(
                binding.btnGreeting2.text.toString(),
                UserChatType.GREETING,
                canModify = false,
                isModifying = false
            ))
            viewModel.setUiState(CreateProfileUiState.InputName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}