package kids.baba.mobile.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.BOTTOMMENU.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.collapse -> {
                        navController.navigate(R.id.collapse_fragment)
                        true
                    }
                    R.id.growth_album -> {
                        navController.navigate(R.id.growth_album_fragment)
                        true
                    }
                    R.id.my_page -> {
                        navController.navigate(R.id.my_page_fragment)
                        true
                    }
                    else -> true
                }
            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.CONTAINER) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = supportFragmentManager.findFragmentById(R.id.CONTAINER)
            if (fragment is GrowthAlbumFragment) fragment.onKeyDown()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}