package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMainBinding
import kids.baba.mobile.presentation.view.fragment.GrowthAlbumFragment
import java.lang.ref.WeakReference

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
//        binding.btvMenu.setOnItemSelectedListener { item ->
//            NavigationUI.onNavDestinationSelected(item =item, navController)
//            navController.popBackStack(item.itemId, inclusive = false)
//            return@setOnItemSelectedListener true
//        }
        binding.btvMenu.setOnItemSelectedListener { item ->
            val builder = NavOptions.Builder().setLaunchSingleTop(true)
            val destinationId = item.itemId
            item.isChecked = true

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

//        val weakReference = WeakReference(this)
//        navController.addOnDestinationChangedListener(
//            object : NavController.OnDestinationChangedListener {
//                override fun onDestinationChanged(
//                    controller: NavController,
//                    destination: NavDestination,
//                    arguments: Bundle?
//                ) {
//                    if(topLevel)
//                }
//
//
//            }
//        )

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