package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.IntroEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val viewModel: IntroViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
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
                Log.d("eventFlow",event.toString())
                when (event) {
                    is IntroEvent.MoveToMain -> {
                        MainActivity.startActivity(this)
                        finish()
                    }
                    is IntroEvent.MoveToLogin -> navController.navigate(R.id.action_onBoardingFragment_to_loginFragment3)
                    is IntroEvent.MoveToSignUp -> navController.navigate(R.id.action_loginFragment_to_signUpFragment)
                    else -> UInt
                }
            }
        }
    }
}