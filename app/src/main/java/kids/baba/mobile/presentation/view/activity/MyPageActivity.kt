package kids.baba.mobile.presentation.view.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityAddbabyBinding
import kids.baba.mobile.presentation.model.MemberUiModel
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog
import kids.baba.mobile.presentation.view.fragment.AddBabyFragment
import kids.baba.mobile.presentation.view.fragment.BabyDetailFragment
import kids.baba.mobile.presentation.view.fragment.InputInviteCodeFragment

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddbabyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddbabyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (intent.getStringExtra("next")) {
            "addBaby" -> showFragment(AddBabyFragment())
            "invite" -> showFragment(InputInviteCodeFragment())
            "babyDetail" -> showFragment(BabyDetailFragment())
        }
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