package kids.baba.mobile.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.view.film.FilmDialog

@AndroidEntryPoint
class GrowthAlbumActivity : AppCompatActivity(), FilmDialog.FilmDialogListener {

    private val tag = "GrowthAlbumActivity"
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_growh_album)

        setNavController()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_growth_album) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onDialogCameraClick(dialog: DialogFragment) {
        Log.d(tag, "CAMERA Click")
    }

    override fun onDialogGalleryClick(dialog: DialogFragment) {
        Log.d(tag, "Gallery Click")
    }


}
