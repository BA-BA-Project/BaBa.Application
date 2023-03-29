package kids.baba.mobile.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.signup.CreateProfileFragmentDirections
import kids.baba.mobile.presentation.viewmodel.IntroViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val viewModel: IntroViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        checkLogin()
        setContentView(R.layout.activity_intro)
        setNavController()
        collectEvent()
    }

    private fun checkLogin(){
        lifecycleScope.launch{
            if(viewModel.checkLogin()){
                MainActivity.startActivity(this@IntroActivity)
                finish()
            }
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
                        val action = LoginFragmentDirections.actionLoginFragmentToTermsAgreeFragment(event.socialToken)
                        navController.navigate(action)
                    }
                    is IntroEvent.MoveToCreateUserProfile -> {
                        val action = TermsAgreeFragmentDirections.actionTermsAgreeFragmentToCreateProfileFragment(event.signToken)
                        navController.navigate(action)
                    }
                    is IntroEvent.MoveToInputBabiesInfo -> {
                        val action = CreateProfileFragmentDirections.actionCreateProfileFragmentToInputBabiesInfoFragment(event.userProfile, event.signToken)
                        navController.navigate(action)
                    }
                }
            }
        }
    }
}