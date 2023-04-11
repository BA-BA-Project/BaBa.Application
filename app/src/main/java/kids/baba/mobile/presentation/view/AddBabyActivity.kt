package kids.baba.mobile.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityAddbabyBinding

class AddBabyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddbabyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddbabyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.getStringExtra("next") == "baby"){
            showFragment(AddBabyFragment())
        }else{
            showFragment(InputInviteCodeFragment())
        }
    }

    private fun showFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
        return true
    }
}