package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R

@AndroidEntryPoint
class GrowthAlbumActivity : AppCompatActivity(){

    private val tag = "GrowthAlbumActivity"
    private lateinit var layout: View
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_growh_album)
        layout = findViewById(R.id.activity_growth_album)
        setNavController()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_growth_album) as NavHostFragment
        navController = navHostFragment.navController
    }

}
