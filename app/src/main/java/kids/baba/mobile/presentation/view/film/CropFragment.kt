package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentCropBinding
import kids.baba.mobile.presentation.viewmodel.CropViewModel

@AndroidEntryPoint
class CropFragment : Fragment() {


    private val TAG = "CropFragment"

    private var _binding: FragmentCropBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }


    private val args: CropFragmentArgs by navArgs()

    val viewModel: CropViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setArgument(args.mediaData)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCropBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}