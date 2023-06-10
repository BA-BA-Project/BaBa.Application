package kids.baba.mobile.presentation.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentEditMemberBinding
import kids.baba.mobile.presentation.event.EditGroupMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.EditMemberViewModel

@AndroidEntryPoint
class EditMemberDialog(val itemClick: () -> Unit) : DialogFragment() {

    private var _binding: DialogFragmentEditMemberBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: EditMemberViewModel by viewModels()

    private lateinit var imm: InputMethodManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BABA_AlbumDialogStyle)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setInputManager()
        setEditTextFocusEvent()
        collectEvent()
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentEditMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is EditGroupMemberEvent.RelationInput -> {
                        keyboardShow(binding.etInput)
                    }

                    is EditGroupMemberEvent.SuccessPatchMemberRelation -> {
                        itemClick()
                        dismiss()
                    }

                    is EditGroupMemberEvent.SuccessDeleteMember -> {
                        itemClick()
                        dismiss()
                    }

                    is EditGroupMemberEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }

                    is EditGroupMemberEvent.DismissDialog -> dismiss()
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun setEditTextFocusEvent() {
        binding.etInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.relationChange(hasFocus)
        }
        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardDown(binding.etInput)
                viewModel.patch()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "EditDialogFragment"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER_KEY"
        const val SELECTED_GROUP_KEY = "SELECTED_GROUP_KEY"
    }
}