package kids.baba.mobile.presentation.view.film

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentWriteTitleBinding
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
import kids.baba.mobile.presentation.viewmodel.CropViewModel
import kids.baba.mobile.presentation.viewmodel.WriteTitleViewModel


@AndroidEntryPoint
class WriteTitleFragment : Fragment(), WriteTitleNavigator {

    private val TAG = "WriteTitleFragment"

    private var _binding: FragmentWriteTitleBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val args: WriteTitleFragmentArgs by navArgs()

    val viewModel: WriteTitleViewModel by viewModels()

    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, args.mediaData.toString())
        viewModel.setArgument(args.mediaData)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWriteTitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val completeBtn = binding.completeBtn

        binding.tbWriteTitle.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewModel = viewModel
        val writeTitleEt = binding.writeTitleEt
        writeTitleEt.requestFocus()

        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.showSoftInput(writeTitleEt, InputMethodManager.SHOW_IMPLICIT)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        writeTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = writeTitleEt.text
                if (text!!.isEmpty()) {
                    completeBtn.apply {
                        isEnabled = false
                        setTextColor(resources.getColor(R.color.inactive_text, null))
                    }
                } else {
                    completeBtn.apply {
                        isEnabled = true
                        setTextColor(resources.getColor(R.color.baba_main, null))
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}