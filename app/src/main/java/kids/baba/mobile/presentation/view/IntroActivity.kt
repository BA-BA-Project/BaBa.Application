package kids.baba.mobile.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityIntroBinding
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.signup.CreateProfileFragmentDirections
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private val viewModel: IntroViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        collectEvent()
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
                    is IntroEvent.MoveToMain -> {
                        MainActivity.startActivity(this)
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
                    is IntroEvent.IntroError -> {
                        Snackbar.make(binding.root, R.string.baba_intro_error,Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
}