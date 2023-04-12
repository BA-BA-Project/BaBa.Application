package kids.baba.mobile.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kids.baba.mobile.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.extras?.getString(USER_NAME) ?: ""
        binding.userName = name

        binding.root.postDelayed({
            MainActivity.startActivity(this)
            finish()
        },2000)

    }

    companion object {
        fun startActivity(context: Context, userName: String) {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USER_NAME, userName)
            context.startActivity(intent)
        }

        private const val USER_NAME = "userName"
    }
}