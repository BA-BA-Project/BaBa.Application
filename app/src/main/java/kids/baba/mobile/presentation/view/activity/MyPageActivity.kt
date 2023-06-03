package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INTENT_PAGE_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_WITH_CODE_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.SETTING_PAGE

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph
    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavController()

        setStartDestination()
    }

    private fun setStartDestination() {
        when (intent.getStringExtra(INTENT_PAGE_NAME)) {
            ADD_BABY_PAGE -> setNavStart(R.id.add_baby_fragment)
            INVITE_WITH_CODE_PAGE -> setNavStart(R.id.input_invite_fragment)
            INVITE_MEMBER_PAGE -> GROUP_NAME.setNavStartWithString(R.id.invite_member_fragment)
            SETTING_PAGE -> setNavStart(R.id.setting_fragment)
            ADD_GROUP_PAGE -> setNavStart(R.id.add_group_fragment)
            BABY_DETAIL_PAGE -> BABY_DETAIL_INFO.setNavStartWithMember(R.id.baby_detail_fragment)
        }
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController.navInflater.inflate(R.navigation.my_page_nav_graph)
    }

    private fun setNavStart(fragment: Int) {
        navGraph.setStartDestination(fragment)
        navController.graph = navGraph
    }

    private fun String.setNavStartWithMember(fragment: Int) {
        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(this, MemberUiModel::class.java)
        } else {
            intent.getParcelableExtra(this)
        }

        key?.let {
            val bundle = bundleOf(this to it)
            navGraph.setStartDestination(fragment)
            navController.setGraph(navGraph, bundle)
        }
    }

    private fun String.setNavStartWithString(fragment: Int) {
        val key = intent.getStringExtra(this)
        key?.let {
            val bundle = bundleOf(this to it)
            navGraph.setStartDestination(fragment)
            navController.setGraph(navGraph, bundle)
        }
    }


    companion object {
        fun startActivity(context: Context, pageName: String) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
            }
            context.startActivity(intent)
        }

        fun startActivityWithMember(
            context: Context,
            pageName: String,
            argumentName: String,
            memberUiModel: MemberUiModel
        ) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
                putExtra(argumentName, memberUiModel)
            }
            context.startActivity(intent)
        }

        fun startActivityWithGroupName(context: Context, pageName: String, argumentName: String, groupName: String) {
            val intent = Intent(context, MyPageActivity::class.java).apply {
                putExtra(INTENT_PAGE_NAME, pageName)
                putExtra(argumentName, groupName)
            }
            context.startActivity(intent)
        }
    }
}
