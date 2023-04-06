package kids.baba.mobile.presentation.view.film

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentWriteTitleBinding
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.FilmViewModel
import kids.baba.mobile.presentation.viewmodel.WriteTitleViewModel
import kotlinx.coroutines.flow.catch


@AndroidEntryPoint
class WriteTitleFragment : Fragment() {

    private val TAG = "WriteTitleFragment"

    private var _binding: FragmentWriteTitleBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: WriteTitleViewModel by viewModels()
    private val activityViewModel: FilmViewModel by activityViewModels()

    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWriteTitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.tbWriteTitle.title = activityViewModel.nowDate.value
        val completeBtn = binding.btnCropComplete
        val writeTitleEt = binding.etWriteTitle

        binding.tbWriteTitle.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.onTextChanged(writeTitleEt, completeBtn)

        setSoftKeyboard(writeTitleEt)

        addListener()
    }

    private fun addListener() {
        binding.btnCropComplete.setOnClickListener {
            viewLifecycleOwner.repeatOnStarted {
                viewModel.complete().catch {
                    Log.e(TAG, it.toString())
                    throw it
                }.collect {
                    activityViewModel.isMoveToSelectCard(it!!)
                }
            }
        }
    }

    private fun setSoftKeyboard(writeTitleEt: EditText) {
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(writeTitleEt, InputMethodManager.SHOW_IMPLICIT)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}