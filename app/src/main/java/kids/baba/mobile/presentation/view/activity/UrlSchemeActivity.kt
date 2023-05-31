package kids.baba.mobile.presentation.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_CODE

class UrlSchemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLinkIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLinkIntent(intent)
    }

    private fun handleDeepLinkIntent(intent: Intent?) {
        intent?.data?.let { deepLinkUri ->
            MyPageActivity.startActivityWithCode(
                this,
                MyPageFragment.INVITE_MEMBER_RESULT_PAGE,
                argumentName = INVITE_CODE,
                inviteCode = extractInviteCode(deepLinkUri)
            )
        }
        finish()
    }

    private fun extractInviteCode(uri: Uri): String {
        val query = uri.query
        val queryParams = query?.split("&")
        var inviteCode = ""
        queryParams?.forEach { param ->
            val keyValue = param.split("=")
            if (keyValue.size == 2 && keyValue[0] == "inviteCode") {
                inviteCode = keyValue[1]
                return@forEach
            }
        }
        return inviteCode
    }
}