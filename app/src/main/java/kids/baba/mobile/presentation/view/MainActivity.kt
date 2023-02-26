package kids.baba.mobile.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.ActivityMainBinding
import kids.baba.mobile.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kakaoLoginExample() // 카카오 로그인 예제
    }

    private fun kakaoLoginExample() {
        Log.e(tag, "디버그 키해시: ${Utility.getKeyHash(this)}")

        binding.kakaoLoginTestBtn.setOnClickListener {
            viewModel.login(this)
        }
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}