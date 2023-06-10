package kids.baba.mobile.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInputInvitecodeBinding
import kids.baba.mobile.presentation.event.BabyInviteCodeEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.InputInviteCodeViewModel

@AndroidEntryPoint
class InputInviteCodeFragment : Fragment() {

    private var _binding: FragmentInputInvitecodeBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InputInviteCodeViewModel by viewModels()

    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputInvitecodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setInputManager()
        setEditTextFocusEvent()
        collectState()
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyInviteCodeEvent.InviteCodeInput -> {
                        if (event.isComplete) {
                            keyboardDown(binding.inputView.etInput)
                        } else {
                            keyboardShow(binding.inputView.etInput)
                        }
                    }

                    is BabyInviteCodeEvent.SuccessAddBabyWithInviteCode -> {
                        val action =
                            InputInviteCodeFragmentDirections.actionInputInviteFragmentToInputInviteResultFragment(
                                event.inviteCode.inviteCode
                            )
                        findNavController().navigate(action)
                    }

                    is BabyInviteCodeEvent.BackButtonClicked -> requireActivity().finish()
                    is BabyInviteCodeEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                }
            }


        }
    }

    private fun setEditTextFocusEvent() {
        binding.inputView.etInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeInviteCodeFocus(hasFocus)
        }
        binding.inputView.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                keyboardDown(binding.inputView.etInput)
                true
            } else {
                false
            }
        }
    }

    private fun setInputManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun keyboardDown(editText: EditText) {
        editText.clearFocus()
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }


    private fun keyboardShow(editText: EditText) {
        editText.requestFocus()
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}