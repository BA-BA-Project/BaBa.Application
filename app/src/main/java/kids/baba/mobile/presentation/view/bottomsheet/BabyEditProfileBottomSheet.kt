package kids.baba.mobile.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditBabyProfileBinding
import kids.baba.mobile.presentation.event.BabyEditEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.BabyEditProfileBottomSheetViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BabyEditProfileBottomSheet @Inject constructor(
    var itemClick: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditBabyProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyEditProfileBottomSheetViewModel by viewModels()


    private lateinit var imm: InputMethodManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setInputManager()
        setEditTextFocusEvent()
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyEditEvent.NameInput -> {
                        keyboardShow(binding.nameView.tvEdit)
                    }

                    is BabyEditEvent.SuccessBabyEdit -> {
                        changeBabyName(event.babyName)
                    }

                    is BabyEditEvent.ShowSnackBar -> {
                        dismiss()
                        showSnackBar(event.message)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditBabyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setInputManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun setEditTextFocusEvent() {
        binding.nameView.tvEdit.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeNameFocus(hasFocus)
        }
        binding.nameView.tvEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardDown(binding.nameView.tvEdit)
                changeBabyName(binding.nameView.tvEdit.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun keyboardDown(editText: EditText) {
        editText.clearFocus()
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }


    private fun keyboardShow(editText: EditText) {
        editText.requestFocus()
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun changeBabyName(name: String) {
        itemClick(name)
        dismiss()
    }


    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BabyEditProfileBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"
    }
}