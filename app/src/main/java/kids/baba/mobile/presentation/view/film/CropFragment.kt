package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentCropBinding
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.CropViewModel
import kids.baba.mobile.presentation.viewmodel.FilmViewModel
import kotlinx.coroutines.flow.catch


@AndroidEntryPoint
class CropFragment : Fragment() {

    private val TAG = "CropFragment"

    private var _binding: FragmentCropBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: CropViewModel by viewModels()
    private val activityViewModel: FilmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCropBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val cropImageView = binding.ivCropImage

        initView(cropImageView)

        addListener(binding.btnCropComplete, cropImageView)
    }

    private fun initView(cropImageView: CropImageView) {
        binding.tbCrop.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.setCropFrame(cropImageView)
    }

    private fun addListener(
        completeBtn: AppCompatButton,
        cropImageView: CropImageView
    ) {
        completeBtn.setOnClickListener {
            viewLifecycleOwner.repeatOnStarted {
                viewModel.cropImage(cropImageView).catch {
                    Log.e(TAG, it.toString())
                    throw it
                }.collect {
                    activityViewModel.isMoveToWriteTitleFromCrop(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}