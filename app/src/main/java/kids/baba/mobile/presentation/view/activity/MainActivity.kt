package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.R.animator
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMainBinding
import kids.baba.mobile.presentation.view.fragment.GrowthAlbumFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        onSupportNavigateUp()
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is GrowthAlbumFragment) fragment.onKeyDown()
            return true
        }
        return super.onKeyDown(keyCode, event)
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