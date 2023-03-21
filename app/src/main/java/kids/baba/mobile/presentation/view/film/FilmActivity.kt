package kids.baba.mobile.presentation.view.film

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import java.io.File

@AndroidEntryPoint
class FilmActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_film)
        setNavController()
    }
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_film) as NavHostFragment
        navController = navHostFragment.navController
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
            activity.startActivity(Intent(activity, FilmActivity::class.java))
        }
    }

}