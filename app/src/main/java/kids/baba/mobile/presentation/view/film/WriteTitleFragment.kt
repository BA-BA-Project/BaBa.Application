package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentWriteTitleBinding
import kids.baba.mobile.presentation.viewmodel.WriteTitleViewModel

@AndroidEntryPoint
class WriteTitleFragment : Fragment(), WriteTitleNavigator {

    private val TAG = "WriteTitleFragment"

    private var _binding: FragmentWriteTitleBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val args: WriteTitleFragmentArgs by navArgs()
//
    val viewModel: WriteTitleViewModel by viewModels()

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


        binding.viewModel = viewModel


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}