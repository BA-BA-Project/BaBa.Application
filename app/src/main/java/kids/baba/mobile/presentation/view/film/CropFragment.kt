package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentCropBinding
import kids.baba.mobile.domain.model.MediaData
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
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

        binding.tbCrop.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.viewModel = viewModel
        val cropImageView = binding.cropImageView

        setCropFrame(cropImageView)
        cropImageView.setImageUriAsync(args.mediaData.mediaPath.toUri())

        val completeBtn = binding.completeBtn

        cropImageView.setOnCropImageCompleteListener { view, result ->

            Log.e(TAG, "CropResult - original uri : ${result.originalUri}" +
                    "cropped content: ${result.uriContent}")

            val data = MediaData(
                mediaName = "cropped",
                mediaPath = result.uriContent.toString(),
                mediaDate = args.mediaData.mediaDate
            )

            Navigation.findNavController(requireActivity(), R.id.fcv_film)
                .navigate(
                    CropFragmentDirections.actionCropFragmentToWriteTitleFragment(
                        data
                    )
                )
        }

        completeBtn.setOnClickListener{
            cropImageView.croppedImageAsync()
        }
    }

    private fun setCropFrame(cropImageView: CropImageView) {
        cropImageView.apply {
            setAspectRatio(1, 1)
            setFixedAspectRatio(true)
        }
        cropImageView.apply {
            guidelines = CropImageView.Guidelines.ON
            cropShape = CropImageView.CropShape.RECTANGLE
            scaleType = CropImageView.ScaleType.FIT_CENTER
            isAutoZoomEnabled = false
            isShowProgressBar = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}