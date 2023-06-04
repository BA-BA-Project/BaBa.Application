package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.databinding.ActivityIntroBinding
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.fragment.LoginFragmentDirections
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
        checkLogin()
        setContentView(binding.root)
        setNavController()
        collectEvent()
    }

    private fun setSplash() {
        binding.root.viewTreeObserver.addOnPreDrawListener (
            object : ViewTreeObserver.OnPreDrawListener{
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

    private fun checkLogin(){
        lifecycleScope.launch{
            if(viewModel.checkLogin()){
                MainActivity.startActivity(this@IntroActivity)
                finish()
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
                        EncryptedPrefs.putString("babyGroupTitle", "아이들") // 처음 로그인 시 아이들이라는 이름으로 아이 그룹 설정.
                        navController.navigate(action)
                    }
                    is IntroEvent.MoveToInputBabiesInfo -> {
                        val action = CreateProfileFragmentDirections.actionCreateProfileFragmentToInputBabiesInfoFragment(event.userProfile, event.signToken)
                        navController.navigate(action)
                    }
                    is IntroEvent.IntroError -> {
                        Snackbar.make(binding.root, R.string.baba_unknown_error,Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    companion object{
        fun startActivity(context: Context) {
            val intent = Intent(context, IntroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}