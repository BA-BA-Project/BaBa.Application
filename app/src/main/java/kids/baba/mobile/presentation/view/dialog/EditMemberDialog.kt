package kids.baba.mobile.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentEditMemberBinding
import kids.baba.mobile.presentation.viewmodel.EditMemberViewModel

@AndroidEntryPoint
class EditMemberDialog(val itemClick: () -> Unit) : DialogFragment() {

    private var _binding: DialogFragmentEditMemberBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: EditMemberViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BABA_AlbumDialogStyle)
        isCancelable = true
        viewModel.dismiss.value = { dismiss() }
        viewModel.patchMember.value = { itemClick() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.deleteView.tvDeleteDesc.setOnClickListener {
            viewModel.delete()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentEditMemberBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "EditDialogFragment"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER_KEY"
        const val SELECTED_MEMBER_RELATION = "SELECTED_MEMBER_RELATION"
    }
}