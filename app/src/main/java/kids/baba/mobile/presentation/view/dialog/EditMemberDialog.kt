package kids.baba.mobile.presentation.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentEditMemberBinding

class EditMemberDialog : DialogFragment() {

    private var _binding: DialogFragmentEditMemberBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BABA_AlbumDialogStyle)
        isCancelable = true
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivCloseEdit.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentEditMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "EditDialogFragment"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER_KEY"
    }
}