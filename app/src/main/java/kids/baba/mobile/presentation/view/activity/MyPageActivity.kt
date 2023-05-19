package kids.baba.mobile.presentation.view.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMyPageBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_BABY_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_GROUP_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_INFO
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.BABY_DETAIL_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INTENT_PAGE_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_WITH_CODE_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.SETTING_PAGE
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

        when (intent.getStringExtra(INTENT_PAGE_NAME)) {
            ADD_BABY_PAGE -> navGraph.setStartDestination(R.id.add_baby_fragment)

            INVITE_WITH_CODE_PAGE -> navGraph.setStartDestination(R.id.input_invite_fragment)

            BABY_DETAIL_PAGE -> {
                val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(BABY_DETAIL_INFO, MemberUiModel::class.java)
                } else {
                    intent.getParcelableExtra(BABY_DETAIL_INFO)
                }
                key?.let {
                    val bundle = Bundle()
                    bundle.putParcelable(BABY_DETAIL_INFO, it)
                    navGraph.addInDefaultArgs(bundle)
                    navGraph.setStartDestination(R.id.baby_detail_fragment)
                }
            }

            INVITE_MEMBER_PAGE -> navGraph.setStartDestination(R.id.invite_member_fragment)
            SETTING_PAGE -> navGraph.setStartDestination(R.id.setting_fragment)
            ADD_GROUP_PAGE -> navGraph.setStartDestination(R.id.add_group_fragment)
        }
        navController.graph = navGraph

    }


//    private fun collectEvent() {
//        repeatOnStarted {
//            viewModel.eventFlow.collect {
//                when (it) {
//                    is MyPageEvent.StartAddBabyPage -> navGraph.setStartDestination(R.id.add_baby_fragment)
//                    is MyPageEvent.StartSettingPage -> navGraph.setStartDestination(R.id.setting_fragment)
//                    is MyPageEvent.StartAddGroupPage -> navGraph.setStartDestination(R.id.add_group_fragment)
//                    is MyPageEvent.StartInvitePage -> navGraph.setStartDestination(R.id.input_invite_fragment)
//                    is MyPageEvent.StartInviteMemberPage -> navGraph.setStartDestination(R.id.invite_member_fragment)
//                    is MyPageEvent.StartBabyDetailPage -> {
//                        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            intent.getParcelableExtra(BABY_DETAIL_PAGE, MemberUiModel::class.java)
//                        } else {
//                            intent.getParcelableExtra(BABY_DETAIL_PAGE)
//                        }
//                        key?.let {
//                            val bundle = Bundle()
//                            bundle.putParcelable(BABY_DETAIL_PAGE, it)
//                            navGraph.addInDefaultArgs(bundle)
//                            navGraph.setStartDestination(R.id.baby_detail_fragment)
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
    }
}