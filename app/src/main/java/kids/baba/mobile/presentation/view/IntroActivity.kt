package kids.baba.mobile.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.IntroUiState
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
        collectUiState()
    }

    private fun setNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_intro) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    IntroUiState.AlreadyLoggedIn -> {
                        MainActivity.startActivity(this)
                        finish()
                    }

                    IntroUiState.NeedToLogin -> {
                        navController.navigate(R.id.action_onBoardingFragment_to_loginFragment)
                    }

                    else -> Unit
                }
            }
        }
    }
}