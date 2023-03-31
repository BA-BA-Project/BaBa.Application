package kids.baba.mobile.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.adapters.AdapterViewBindingAdapter.setOnItemSelectedListener
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.BOTTOMMENU.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.collapse -> showFragment(CollapseFragment())
                    R.id.growth_album -> showFragment(GrowthAlbumFragment())
                    R.id.my_page -> showFragment(MyPageFragment())
                    else -> true
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = supportFragmentManager.findFragmentById(R.id.CONTAINER)
            if (fragment is GrowthAlbumFragment) fragment.onKeyDown()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun showFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.CONTAINER, fragment)
            commit()
        }
        return true
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}