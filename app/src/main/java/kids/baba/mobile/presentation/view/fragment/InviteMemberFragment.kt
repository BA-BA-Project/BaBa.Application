package kids.baba.mobile.presentation.view.fragment

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.*
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.domain.model.RelationInfo
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
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

            // 템플릿 객체.
            val defaultText = TextTemplate(
                text = """
        바바를 통해 친구네 아이의 성장을
        확인하고 친구와 소통해봐요!
    """.trimIndent(),
                // TODO: Kakao developers 에서 Web 사이트 도메인을 playstore 다운로드 링크로 변경해야 함
                //  앱이 다운로드되어 있을 때
                //      1. 로그인을 한 적이 없을 때 그냥 실행
                //      2. 로그인을 한 적이 있을 때 초대 코드 입력 쪽으로 이동.
                //  앱이 다운로드되어있지 않을 때 playstore 링크로.-
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                ),
                buttons = null
            )


            // 카카오톡 설치여부 확인
            if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
                // 카카오톡으로 카카오톡 공유 가능
                ShareClient.instance.shareDefault(requireContext(), defaultText) { sharingResult, error ->
                    if (error != null) {
                        Log.e("KAKAO Share", "카카오톡 공유 실패", error)
                    } else if (sharingResult != null) {
                        Log.d("KAKAO Share", "카카오톡 공유 성공 ${sharingResult.intent}")
                        startActivity(sharingResult.intent)

                        // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                        Log.w("KAKAO Share", "Warning Msg: ${sharingResult.warningMsg}")
                        Log.w("KAKAO Share", "Argument Msg: ${sharingResult.argumentMsg}")
                    }
                }
            } else {
                // 카카오톡 미설치: 웹 공유 사용 권장
                // 웹 공유 예시 코드
                val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultText)

                // CustomTabs으로 웹 브라우저 열기

                // 1. CustomTabsServiceConnection 지원 브라우저 열기  ex) Chrome, 삼성 인터넷
                try {
                    KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
                } catch (e: UnsupportedOperationException) {
                    // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                    Log.e("KAKAO Share", "CustomTabs open fail: $e")
                }

                // 2. CustomTabsServiceConnection 미지원 브라우저 열기 ex) 네이버, 카카오, UC
                try {
                    KakaoCustomTabsClient.open(requireContext(), sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                    Log.e("KAKAO Share", "Browser open fail: $e")
                }
            }

            findNavController().navigate(R.id.action_invite_member_fragment_to_invite_member_result_fragment)
        }

        binding.btnCopyCode.setOnClickListener{
            val relationGroup = binding.inputGroupView.etInput.text.toString()
            val relationName = binding.inputRelationView.etInput.text.toString()
            viewModel.copyCode(relationInfo = RelationInfo(relationGroup, relationName))
        }

        collectEvent()

    }

    private fun collectEvent(){
        repeatOnStarted {
            viewModel.eventFlow.collect{ event ->
                when(event){
                    is InviteMemberEvent.ShowSnackBar -> showSnackBar(event.msg)
                    is InviteMemberEvent.SuccessCopyInviteCode -> {
                        Log.e("InviteMemberFragment", "${event.inviteCode.inviteCode} 을 클립보드에 복사.")

                        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("inviteCode", event.inviteCode.inviteCode)
                        clipboard.setPrimaryClip(clip)
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
        return binding.root
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

}