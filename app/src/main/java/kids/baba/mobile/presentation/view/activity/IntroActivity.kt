package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityIntroBinding
import kids.baba.mobile.presentation.event.DeepLinkEvent
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.fragment.LoginFragmentDirections
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kids.baba.mobile.presentation.view.fragment.TermsAgreeFragmentDirections
import kids.baba.mobile.presentation.view.signup.CreateProfileFragmentDirections
import kids.baba.mobile.presentation.viewmodel.IntroViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private val viewModel: IntroViewModel by viewModels()

    private lateinit var navController: NavController

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setSplash()
        setContentView(binding.root)
        checkLogin(intent)
        setNavController()
        collectDeeplinkEvent()
        collectEvent()
        Log.d("keyHash", Utility.getKeyHash(this))
    }

    private fun collectDeeplinkEvent() {
        repeatOnStarted {
            viewModel.deepLinkEvent.collect {
                when (it) {
                    is DeepLinkEvent.RequestLogin -> requestLogin()
                    is DeepLinkEvent.GoToInviteResultPage -> goToInviteResultPage()
                    is DeepLinkEvent.NetworkError -> showToast(R.string.baba_network_failed)
                    is DeepLinkEvent.Failure -> showToast(R.string.baba_login_failed)
                }
            }
        }
    }

    private fun setSplash() {
        binding.root.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun checkLogin(intent: Intent?) {
        lifecycleScope.launch {
            if (viewModel.checkLogin()) {
                if (intent?.action != Intent.ACTION_VIEW) {
                    MainActivity.startActivity(this@IntroActivity)
                    finish()
                } else {
                    viewModel.handleDeeplink()
                }
            } else {
                if (intent?.action == Intent.ACTION_VIEW) requestLogin()
            }
            isReady = true
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_intro) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is IntroEvent.MoveToWelcome -> {
                        WelcomeActivity.startActivity(this, event.name)
                        finish()
                    }

                    is IntroEvent.MoveToLogin -> navController.navigate(R.id.action_onBoardingFragment_to_loginFragment3)
                    is IntroEvent.MoveToAgree -> {
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToTermsAgreeFragment(event.socialToken)
                        navController.navigate(action)
                    }

                    is IntroEvent.MoveToCreateUserProfile -> {
                        val action =
                            TermsAgreeFragmentDirections.actionTermsAgreeFragmentToCreateProfileFragment(
                                event.signToken
                            )
                        navController.navigate(action)
                    }

                    is IntroEvent.MoveToInputBabiesInfo -> {
                        val action =
                            CreateProfileFragmentDirections.actionCreateProfileFragmentToInputBabiesInfoFragment(
                                event.userProfile,
                                event.signToken
                            )
                        navController.navigate(action)
                    }

                    is IntroEvent.IntroError -> {
                        Snackbar.make(
                            binding.root,
                            R.string.baba_unknown_error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun requestLogin() {
        showToast(R.string.login_request)
        finish()
    }

    private fun goToInviteResultPage() {
        intent.data?.let { deepLinkUri ->
            MyPageActivity.startActivityWithCode(
                this,
                MyPageFragment.INVITE_MEMBER_RESULT_PAGE,
                argumentName = MyPageFragment.INVITE_CODE,
                inviteCode = extractInviteCode(deepLinkUri)
            )
        }
        finish()
    }

    private fun showToast(@StringRes text: Int) = Toast.makeText(
        this,
        getString(text),
        Toast.LENGTH_LONG
    ).show()

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

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}