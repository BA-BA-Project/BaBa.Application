package kids.baba.mobile.presentation.view.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.fragment.MyPageFragmentDirections
import kids.baba.mobile.presentation.viewmodel.MyPageActivityViewModel

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMyPageBinding
    private val viewModel: MyPageActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()
        instance = this
        //collectEvent()
        when (intent.getStringExtra("next")) {
            "addBaby" -> navigate(R.id.action_my_page_fragment_to_add_baby_fragment)
            "invite" -> navigate(R.id.action_my_page_fragment_to_input_invite_fragment)
            "babyDetail" -> {
                val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra("baby", MemberUiModel::class.java)
                } else {
                    intent.getParcelableExtra("baby")
                }
                key?.let {
                    val action = MyPageFragmentDirections.actionMyPageFragmentToBabyDetailFragment(it)
                    navigate(action)
                }
            }

            "member" -> navigate(R.id.action_my_page_fragment_to_invite_member_fragment)
            "setting" -> navigate(R.id.action_my_page_fragment_to_setting_fragment)
            "addGroup" -> navigate(R.id.action_my_page_fragment_to_add_group_fragment)
        }
    }
    private fun navigate(to:Int){
        binding.btvMenu.visibility = View.GONE
        navController.navigate(to)
    }

    private fun navigate(action:NavDirections){
        binding.btvMenu.visibility = View.GONE
        navController.navigate(action)
    }

    fun bottomNavOn() {
        binding.btvMenu.visibility = View.VISIBLE
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is MyPageEvent.MoveToAddBabyPage -> navController.navigate(R.id.action_my_page_fragment_to_add_baby_fragment)
                    is MyPageEvent.MoveToSettingPage -> navController.navigate(R.id.action_my_page_fragment_to_setting_fragment)
                    is MyPageEvent.MoveToAddGroupPage -> navController.navigate(R.id.action_my_page_fragment_to_add_group_fragment)
                    is MyPageEvent.MoveToInvitePage -> navController.navigate(R.id.action_my_page_fragment_to_input_invite_fragment)
                    is MyPageEvent.MoveToInviteMemberPage -> navController.navigate(R.id.action_my_page_fragment_to_invite_member_fragment)
                    is MyPageEvent.MoveToBabyDetailPage -> {
                        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra("baby", MemberUiModel::class.java)
                        } else {
                            intent.getParcelableExtra("baby")
                        }
                        key?.let {
                            val action =
                                MyPageFragmentDirections.actionMyPageFragmentToBabyDetailFragment(it)
                            navController.navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        binding.btvMenu.setupWithNavController(navController)
    }

    companion object {
        lateinit var instance: MyPageActivity
    }
}