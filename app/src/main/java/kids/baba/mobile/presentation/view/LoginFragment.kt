package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentLoginBinding
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.LoginUiState
import kids.baba.mobile.presentation.viewmodel.IntroViewModel
import kids.baba.mobile.presentation.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val activityViewModel: IntroViewModel by activityViewModels()
    val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.loginUiState.collect { uiState ->
                when (uiState) {
                    is LoginUiState.LoginCanceled -> showSnackBar(R.string.kakao_login_canceled)
                    is LoginUiState.NeedToSignUp -> {
                        val signToken = uiState.signToken
                        //가입 화면으로 이동
                    }
                    is LoginUiState.Success -> activityViewModel.isLoginSuccess()
                    is LoginUiState.Failure -> showSnackBar(R.string.baba_login_failed)
                    is LoginUiState.Loading -> Unit
                }
            }
        }
    }

    private fun initView() {
        binding.viewModel = viewModel
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}