package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.databinding.FragmentTextInputBinding
import kids.baba.mobile.presentation.model.ChatUserType
import kids.baba.mobile.presentation.state.SignUpUiState
import kids.baba.mobile.presentation.viewmodel.SignUpViewModel

class TextInputFragment : Fragment() {
    private var _binding: FragmentTextInputBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: SignUpViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.apply {
//            etTextInput.setOnEditorActionListener(getEditorActionListener(tvSend))
//
//            tvSend.setOnClickListener {
//                viewModel.addChat(ChatUserType.USER,etTextInput.text.toString(), true)
//                if (viewModel.signUpUiState.value == SignUpUiState.InputName) {
//                    viewModel.setUiState(SignUpUiState.SelectProfile)
//                }
//                etTextInput.setText("")
//            }
//        }
    }

    private fun getEditorActionListener(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            false
        }
    }
}
