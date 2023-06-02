package kids.baba.mobile.presentation.view.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_CODE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_RESULT_PAGE
import kids.baba.mobile.presentation.viewmodel.InviteMemberViewModel

@AndroidEntryPoint
class InviteMemberFragment : Fragment() {

    private var _binding: FragmentInviteMemberBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteMemberViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnInvite.setOnClickListener {
            val relationGroup = binding.inputGroupView.etInput.text.toString()
            val relationName = binding.inputRelationView.etInput.text.toString()
            viewModel.sendInvitation(relationInfo = RelationInfo(relationGroup, relationName))
        }

        binding.btnCopyCode.setOnClickListener {
            val relationGroup = binding.inputGroupView.etInput.text.toString()
            val relationName = binding.inputRelationView.etInput.text.toString()
            viewModel.copyCode(relationInfo = RelationInfo(relationGroup, relationName))
        }
        binding.inputGroupView.etInput.setText(viewModel.groupName.value)
        binding.inputGroupView.etInput.isEnabled = false
        binding.inputRelationView.etInput.addTextChangedListener {

        }
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

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is InviteMemberEvent.ShowSnackBar -> showSnackBar(event.msg)
                    is InviteMemberEvent.SuccessCopyInviteCode -> {
                        Log.e("InviteMemberFragment", "${event.inviteCode.inviteCode} 을 클립보드에 복사.")
                        val clipboard =
                            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("inviteCode", event.inviteCode.inviteCode)
                        clipboard.setPrimaryClip(clip)
                    }

                    is InviteMemberEvent.SuccessSendInvitation -> {
                        send(event.inviteCode.inviteCode)
                        MyPageActivity.startActivityWithCode(
                            requireContext(),
                            INVITE_MEMBER_RESULT_PAGE, INVITE_CODE, event.inviteCode.inviteCode
                        )
                    }
                }

            }
        }
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
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

}