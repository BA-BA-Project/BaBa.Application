package kids.baba.mobile.presentation.view.film

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentCameraBinding
import kids.baba.mobile.presentation.event.GetPictureEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
import kids.baba.mobile.presentation.viewmodel.FilmViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment @Inject constructor() : Fragment() {

    private val TAG = "CameraFragment"

    private var _binding: FragmentCameraBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: CameraViewModel by viewModels()
    private val activityViewModel: FilmViewModel by activityViewModels()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            // 선택된 사진이 있을 경우
            Log.e(TAG, "choose picture $uri")

            handlePickerResponse(uri)
        } else {
            // 선택된 사진이 없을 경우
            Log.e(TAG, "There is no picture chosen ")
        }
    }


    private fun handlePickerResponse(savedUri: Uri) {
        lifecycleScope.launch {
            viewModel.pickerSavePhoto(savedUri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val viewFinder = binding.viewFinder
        viewFinder.post {
            viewModel.setUpCamera(viewFinder, viewLifecycleOwner)
        }

        addListener(viewFinder)
        collectEvent()

    }

    private fun addListener(viewFinder: PreviewView) {
        binding.tbCamera.setNavigationOnClickListener {
            requireActivity().finish()
        }
        binding.btnAlbum.setOnClickListener { goToAlbum() }
        binding.btnToggle.setOnClickListener { viewModel.toggleCamera(viewFinder, viewLifecycleOwner) }
    }


    ////////////////
    private fun goToAlbum() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is GetPictureEvent.GetFromCamera -> activityViewModel.isMoveToWriteTitleFromCamera(event.mediaData)
                    is GetPictureEvent.GetFromGallery -> activityViewModel.isMoveToCrop(event.mediaData)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}