package kids.baba.mobile.presentation.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentAskBinding
import kids.baba.mobile.presentation.binding.ComposableTopViewData

@AndroidEntryPoint
class AskFragment : Fragment() {
    private var _binding: FragmentAskBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setAskBtn()
    }

    private fun setBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.title = getString(R.string.ask)
        binding.topViewData = ComposableTopViewData(
            onBackButtonClickEventListener = {
                findNavController().navigateUp()
            }
        )
    }

    private fun setAskBtn() {
        binding.tvDeleteAccountRequest.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/baba-agree/%EA%B3%84%EC%A0%95-%EB%B0%8F-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%82%AD%EC%A0%9C-%EC%9A%94%EC%B2%AD"))
            startActivity(intent)
        }

        binding.btnComplete.setOnClickListener {
            val email = getString(R.string.baba_email)
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            }
            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.google.com/"))
                startActivity(webIntent)
            }
        }
    }
}