package kids.baba.mobile.presentation.view.film

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.FilmViewModel

@AndroidEntryPoint
class FilmActivity : AppCompatActivity() {

    private val viewModel: FilmViewModel by viewModels()

    private lateinit var navController: NavController

    private val TAG = "FilmActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_film)
        setNavController()
        collectEvent()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_film) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.filmEventFlow.collect { event ->
                Log.d(TAG, event.toString())
                when (event) {
                    is FilmEvent.StartOnCamera -> {
                        Log.d(TAG, "START ON CAMERA")
                    }

                    is FilmEvent.MoveToCrop -> {
                        Log.e(TAG, "MOVE TO CROP")
                        val action = CameraFragmentDirections.actionCameraFragmentToCropFragment(event.mediaData)
                        navController.navigate(action)
                    }
                    is FilmEvent.MoveToWriteTitleFromCamera -> {
                        Log.e(TAG, "MOVE TO WRITE TITLE FROM CAMERA")
                        val action = CameraFragmentDirections.actionCameraFragmentToWriteTitleFragment(event.mediaData)
                        navController.navigate(action)
                    }
                    is FilmEvent.MoveToWriteTitleFromCrop -> {
                        Log.e(TAG, "MOVE TO WRITE TITLE FROM CROP")
                        val action = CropFragmentDirections.actionCropFragmentToWriteTitleFragment(event.mediaData)
                        navController.navigate(action)
                    }

                    is FilmEvent.MoveToSelectCard -> {
                        Log.e(TAG, "MOVE TO SELECT CARD")
                    }
                }

            }
        }
    }


    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, FilmActivity::class.java)
            context.startActivity(intent)
        }
    }

}