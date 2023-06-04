package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.R.animator
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMainBinding
import kids.baba.mobile.presentation.event.DeepLinkEvent
import kids.baba.mobile.presentation.view.fragment.MyPageFragment
import kids.baba.mobile.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        onSupportNavigateUp()
        handleDeepLinkIntent(intent)
        collectEvent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLinkIntent(intent)
    }

    private fun collectEvent() {
        lifecycleScope.launch {
            viewModel.deepLinkEvent.collect {
                when (it) {
                    is DeepLinkEvent.RequestLogin -> goToLoginPage()
                    is DeepLinkEvent.GoToInviteResultPage -> goToInviteResultPage()
                    is DeepLinkEvent.NetworkError -> showToast(R.string.baba_network_failed)
                    is DeepLinkEvent.Failure -> showToast(R.string.baba_login_failed)
                }
            }
        }
    }

    private fun goToLoginPage() {
        showToast(R.string.login_request)
        IntroActivity.startActivity(this@MainActivity)
    }

    private fun goToInviteResultPage() {
        intent.data?.let { deepLinkUri ->
            MyPageActivity.startActivityWithCode(
                this@MainActivity,
                MyPageFragment.INVITE_MEMBER_RESULT_PAGE,
                argumentName = MyPageFragment.INVITE_CODE,
                inviteCode = extractInviteCode(deepLinkUri)
            )
        }
        finish()
    }

    private fun showToast(@StringRes text: Int) = Toast.makeText(
        this@MainActivity,
        getString(text),
        Toast.LENGTH_LONG
    ).show()

    private fun handleDeepLinkIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            viewModel.handleDeeplink()
        }
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

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        binding.btvMenu.setupWithNavController(navController)

        binding.btvMenu.setOnItemSelectedListener { item ->
            val builder = NavOptions.Builder().setLaunchSingleTop(true)
            val destinationId = item.itemId
            item.isChecked = true

            builder.setEnterAnim(animator.nav_default_enter_anim)
                .setExitAnim(animator.nav_default_exit_anim)
                .setPopEnterAnim(animator.nav_default_pop_enter_anim)
                .setPopExitAnim(animator.nav_default_pop_exit_anim)

            if (item.order and Menu.CATEGORY_SECONDARY == 0) {
                builder.setPopUpTo(
                    navController.graph.findStartDestination().id,
                    inclusive = false,
                    saveState = false
                )
            }
            val options = builder.build()
            return@setOnItemSelectedListener try {
                navController.navigate(destinationId, null, options)
                (navController.currentDestination?.id ?: false) == destinationId

            } catch (e: java.lang.IllegalArgumentException) {
                false
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}