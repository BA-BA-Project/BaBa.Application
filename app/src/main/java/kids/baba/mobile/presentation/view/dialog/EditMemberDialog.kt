package kids.baba.mobile.presentation.view.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.DialogFragmentEditMemberBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.viewmodel.EditMemberViewModel

@AndroidEntryPoint
class EditMemberDialog : DialogFragment() {

    private var _binding: DialogFragmentEditMemberBinding? = null
    private var member: MemberUiModel? = null
    private var relation: String? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: EditMemberViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.BABA_AlbumDialogStyle)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        member = this.arguments?.getParcelable<MemberUiModel>(SELECTED_MEMBER_KEY)
        relation = this.arguments?.getString(SELECTED_MEMBER_RELATION)
        viewModel.name.value = member?.name ?: ""
        binding.tvRelation.text = relation
        Log.e("member","$member")
        binding.ivCloseEdit.setOnClickListener {
            dismiss()
        }
        binding.tvEditComplete.setOnClickListener {
            val relation = binding.etInput.text.toString()
            viewModel.patch(member!!.memberId, relation)
            dismiss()
        }
        binding.deleteView.tvDeleteDesc.setOnClickListener {
            viewModel.delete(member!!.memberId)
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