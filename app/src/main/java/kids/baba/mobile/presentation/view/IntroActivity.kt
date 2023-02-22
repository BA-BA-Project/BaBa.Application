package kids.baba.mobile.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.state.IntroUiState
import kids.baba.mobile.presentation.view.onboarding.OnBoardingFragment
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private val viewModel: IntroViewModel by viewModels()
    private val onBoardingFragment by lazy {
        OnBoardingFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        collectUiState()
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
                        //로그인 화면으로
                    }

                    IntroUiState.NeedToOnboard -> {
                        supportFragmentManager.commit {
                            add(R.id.fcv_intro, onBoardingFragment)
                        }
                    }

                    else -> {
                    }
                }
            }
        }
    }
}