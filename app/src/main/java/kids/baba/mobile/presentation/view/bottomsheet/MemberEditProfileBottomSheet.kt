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
import kids.baba.mobile.databinding.BottomSheetEditProfileBinding
import kids.baba.mobile.presentation.adapter.ColorAdapter
import kids.baba.mobile.presentation.adapter.IconAdapter
import kids.baba.mobile.presentation.event.EditMemberProfileEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import kids.baba.mobile.presentation.viewmodel.EditMemberProfileBottomSheetViewModel

@AndroidEntryPoint
class MemberEditProfileBottomSheet(
    val itemClick: () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetEditProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: EditMemberProfileBottomSheetViewModel by viewModels()

    private lateinit var imm: InputMethodManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setInputManager()
        setEditTextFocusEvent()
        setIconButton()
        setColorButton()
        collectEvent()
    }

    private fun setIconButton() {
        val icons = mutableListOf<UserIconUiModel>().apply {
            addAll(
                UserProfileIconUiModel
                    .values()
                    .dropLast(1)
                    .map { UserIconUiModel(it, "#EDEDED") }
            )
        }

        val adapter = IconAdapter(
            itemClick = { item ->
                viewModel.setIcon(item)
            }
        )

        binding.iconView.iconContainer.adapter = adapter
        adapter.submitList(icons)
    }

    private fun setColorButton() {
        val colors = mutableListOf<ColorUiModel>().apply {
            addAll(
                ColorModel
                    .values()
                    .map { ColorUiModel(it.name, it.colorCode) }
            )
        }

        val adapter = ColorAdapter(
            itemClick = { color ->
                viewModel.setColor(color)
            }
        )
        binding.colorView.colorContainer.adapter = adapter
        adapter.submitList(colors)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is EditMemberProfileEvent.NameInput -> {
                        keyboardShow(binding.nameView.tvEdit)
                    }

                    is EditMemberProfileEvent.IntroInput -> {
                        keyboardShow(binding.introView.etInput)
                    }

                    is EditMemberProfileEvent.SuccessEditMemberProfile -> {
                        itemClick()
                        dismiss()
                    }

                    is EditMemberProfileEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun setInputManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun setEditTextFocusEvent() {
        binding.nameView.tvEdit.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeNameFocus(hasFocus)
        }
        binding.introView.etInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeIntroFocus(hasFocus)
        }
        binding.nameView.tvEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardDown(binding.nameView.tvEdit)
                true
            } else {
                false
            }
        }

        binding.introView.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardDown(binding.introView.etInput)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MemberEditProfileBottomSheet"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER"

    }
}