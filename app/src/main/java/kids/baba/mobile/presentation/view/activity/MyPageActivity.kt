package kids.baba.mobile.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        when (intent.getStringExtra("next")) {
            "addBaby" -> navController.navigate(R.id.action_my_page_fragment_to_add_baby_fragment)
            "invite" -> navController.navigate(R.id.action_my_page_fragment_to_input_invite_fragment)
            "babyDetail" -> navController.navigate(R.id.action_my_page_fragment_to_baby_detail_fragment)
            "member" -> navController.navigate(R.id.action_my_page_fragment_to_invite_member_fragment)
            "setting" -> navController.navigate(R.id.action_my_page_fragment_to_setting_fragment)
            "addGroup" -> navController.navigate(R.id.action_my_page_fragment_to_add_group_fragment)
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
    }
}