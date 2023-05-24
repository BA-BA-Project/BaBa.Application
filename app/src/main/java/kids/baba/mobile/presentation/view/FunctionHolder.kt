package kids.baba.mobile.presentation.view

import android.util.Log
import androidx.navigation.NavController


interface FunctionHolder {
    fun click() {
        Log.e("function", "not implemented!!")
    }

    fun navigateUp(navController: NavController) {
        navController.navigateUp()
    }
}
