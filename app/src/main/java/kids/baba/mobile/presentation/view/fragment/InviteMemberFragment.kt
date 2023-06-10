package kids.baba.mobile.presentation.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_CODE
import kids.baba.mobile.presentation.viewmodel.InviteMemberViewModel

@AndroidEntryPoint
class InviteMemberFragment : Fragment() {

    private var _binding: FragmentInviteMemberBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteMemberViewModel by viewModels()

    private lateinit var imm: InputMethodManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setInputManager()
        setEditTextFocusEvent()
        collectEvent()
    }

    private fun send(inviteCode: String) {
        val defaultFeed = FeedTemplate(
            content = Content(
                title = "아이와 어른의 성장일기",
                description = "앱 다운로드 후 초대코드를 입력해 가입해봐요",
                imageUrl = "https://i.ibb.co/xLy2RsJ/2023-05-31-3-27-51.png",
                link = Link(
                    androidExecutionParams = mapOf(INVITE_CODE to inviteCode)
                )
            )
        )
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareDefault(requireContext(), defaultFeed) { result, t ->
                if (t != null) {
                    Log.e("fail", "${t.message}")
                } else if (result != null) {
                    startActivity(result.intent)
                }
            }
        }

    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is InviteMemberEvent.ShowSnackBar -> showSnackBar(event.message)

                    is InviteMemberEvent.GoToBack -> {
                        requireActivity().finish()
                    }

                    is InviteMemberEvent.RelationInput -> {
                        if (event.isComplete) {
                            keyboardDown(binding.inputRelationView.etInput)
                        } else {
                            keyboardShow(binding.inputRelationView.etInput)
                        }
                    }

                    is InviteMemberEvent.InviteWithKakao -> {
                        send(event.inviteCode.inviteCode)
                        requireActivity().finish()
                    }

                    is InviteMemberEvent.CopyInviteCode -> {
                        val clipboard =
                            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("inviteCode", event.inviteCode.inviteCode)
                        clipboard.setPrimaryClip(clip)
                    }
                }
            }
        }
    }

    private fun setInputManager() {
        imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun setEditTextFocusEvent() {
        binding.inputRelationView.etInput.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeRelationFocus(hasFocus)
        }
        binding.inputRelationView.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keyboardDown(binding.inputRelationView.etInput)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

}