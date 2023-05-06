package kids.baba.mobile.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.fragment.AddBabyFragment
import kids.baba.mobile.presentation.view.fragment.BabyDetailFragment
import kids.baba.mobile.presentation.view.fragment.FragmentSetting
import kids.baba.mobile.presentation.view.fragment.InputInviteCodeFragment
import kids.baba.mobile.presentation.view.fragment.InviteMemberFragment

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
            "invite" -> showFragment(InputInviteCodeFragment())
            "babyDetail" -> showFragment(BabyDetailFragment())
            "member" -> showFragment(InviteMemberFragment())
            "setting" -> navController.navigate(R.id.action_my_page_fragment_to_setting_fragment)
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun showFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            val bundle = Bundle()
            bundle.putParcelable(
                BabyDetailFragment.SELECTED_BABY_KEY,
                intent.getParcelableExtra<MemberUiModel>("baby")
            )
            fragment.arguments = bundle
            commit()
        }
        return true
    }
}