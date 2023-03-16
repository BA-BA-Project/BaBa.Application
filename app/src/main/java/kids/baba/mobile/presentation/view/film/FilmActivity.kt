package kids.baba.mobile.presentation.view.film

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.presentation.event.FilmEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
import java.io.File

@AndroidEntryPoint
class FilmActivity : AppCompatActivity() {
private val TAG = "FilmActivity"
    private lateinit var navController: NavController

    val viewModel: CameraViewModel by viewModels()
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
            Log.e(TAG, "collectEvent() called. this is in repeatOnStarted block")
            viewModel.eventFlow.collect { event ->
                Log.e(TAG, event.toString())
                when (event) {
                    is FilmEvent.StartOnCamera -> {
                        Log.e(TAG, "Just STart on Camera")
                    }
                    is FilmEvent.MoveToWriteTitle -> {
                        Log.e(TAG, "Mote To Write Title")
                        val action = CameraFragmentDirections.actionCameraFragmentToWriteTitleFragment(event.mediaData)
                        navController.navigate(action)
                    }

                    is FilmEvent.MoveToGallery -> {
                        Log.e(TAG, "GO To Gallery")
                    }
                    else -> Log.e(TAG, "FilmEvent Else" )

                }

            }
        }
    }


    companion object {
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext

            val mediaDir = context.getExternalFilesDirs(Environment.DIRECTORY_PICTURES).firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply {
                    mkdirs()
                }
            }

            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

        fun open(activity: Activity) {
            activity.startActivity(Intent(activity, FilmActivity::class.java)).also {
//                activity.finish()
            }
        }
    }

}