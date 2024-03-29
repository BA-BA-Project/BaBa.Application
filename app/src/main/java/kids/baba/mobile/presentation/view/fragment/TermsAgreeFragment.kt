package kids.baba.mobile.presentation.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentTermsAgreeBinding
import kids.baba.mobile.presentation.adapter.TermsAdapter
import kids.baba.mobile.presentation.event.TermsAgreeEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.TermsUiModel
import kids.baba.mobile.presentation.viewmodel.IntroViewModel
import kids.baba.mobile.presentation.viewmodel.TermsAgreeViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TermsAgreeFragment : Fragment(), TermsAdapter.TermsClickListener {

    private var _binding: FragmentTermsAgreeBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val activityViewModel: IntroViewModel by activityViewModels()
    private val viewModel: TermsAgreeViewModel by viewModels()

    private lateinit var termsAdapter: TermsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsAgreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()
        initSignToken()

        setNextBtn()
        setTermsCheckBox()
        collectEvent()
        collectSignToken()
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initSignToken() {
        viewModel.setSignToken()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is TermsAgreeEvent.ShowSnackBar -> showSnackBar(event.text)
                }
            }
        }
    }

    private fun setTermsCheckBox() {
        termsAdapter = TermsAdapter(this)
        binding.rvTerms.adapter = termsAdapter

        viewLifecycleOwner.repeatOnStarted {
            viewModel.termsList.collect {
                termsAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.isAllChecked.collect {
                binding.cbAllAgree.isChecked = it
            }
        }
    }

    private fun setNextBtn() {
        binding.btnSignUpStart.setOnClickListener {
            if (viewModel.checkEssentialAllChecked()) {
                viewModel.getSignToken()
            } else {
                showSnackBar(R.string.required_terms_acceptance)
            }
        }
    }

    private fun collectSignToken() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.signToken.collectLatest {
                if (it.isNotEmpty()) {
                    activityViewModel.isSignUpStart(it)
                }
            }
        }
    }


    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onTermsClickListener(termsData: TermsUiModel, checked: Boolean) {
        viewModel.changeTermsAgree(termsData, checked)
    }

    override fun onDetailContentClickListener(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}