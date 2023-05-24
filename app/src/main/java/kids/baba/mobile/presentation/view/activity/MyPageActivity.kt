package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.viewmodel.MyPageActivityViewModel

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph
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
            "addBaby" -> navGraph.setStartDestination(R.id.add_baby_fragment)

            "invite" -> navGraph.setStartDestination(R.id.input_invite_fragment)

            "babyDetail" -> {
                val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra("baby", MemberUiModel::class.java)
                } else {
                    intent.getParcelableExtra("baby")
                }
                key?.let {
                    val bundle = Bundle()
                    bundle.putParcelable("baby", it)
                    navGraph.addInDefaultArgs(bundle)
                    navGraph.setStartDestination(R.id.baby_detail_fragment)
                }
            }

            "member" -> navGraph.setStartDestination(R.id.invite_member_fragment)
            "setting" -> navGraph.setStartDestination(R.id.setting_fragment)
            "addGroup" -> navGraph.setStartDestination(R.id.add_group_fragment)
        }
        navController.graph = navGraph

    }


//    private fun collectEvent() {
//        repeatOnStarted {
//            viewModel.eventFlow.collect {
//                when (it) {
//                    is MyPageEvent.MoveToAddBabyPage -> navController.navigate(R.id.action_my_page_fragment_to_add_baby_fragment)
//                    is MyPageEvent.MoveToSettingPage -> navController.navigate(R.id.action_my_page_fragment_to_setting_fragment)
//                    is MyPageEvent.MoveToAddGroupPage -> navController.navigate(R.id.action_my_page_fragment_to_add_group_fragment)
//                    is MyPageEvent.MoveToInvitePage -> navController.navigate(R.id.action_my_page_fragment_to_input_invite_fragment)
//                    is MyPageEvent.MoveToInviteMemberPage -> navController.navigate(R.id.action_my_page_fragment_to_invite_member_fragment)
//                    is MyPageEvent.MoveToBabyDetailPage -> {
//                        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            intent.getParcelableExtra("baby", MemberUiModel::class.java)
//                        } else {
//                            intent.getParcelableExtra("baby")
//                        }
//                        key?.let {
//                            val action =
//                                MyPageFragmentDirections.actionMyPageFragmentToBabyDetailFragment(it)
//                            navController.navigate(action)
//                        }
//                    }
//                }
//            }
//        }
//    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController.navInflater.inflate(R.navigation.my_page_nav_graph)
    }

    companion object {
        lateinit var instance: MyPageActivity

        fun startActivity(context: Context, next: String) {
            context.startActivity(Intent(context, MyPageActivity::class.java).apply {
                putExtra("next", next)
            })
        }
    }
}