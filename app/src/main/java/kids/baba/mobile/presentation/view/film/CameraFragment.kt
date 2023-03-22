package kids.baba.mobile.presentation.view.film

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentCameraBinding
import kids.baba.mobile.presentation.viewmodel.CameraViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment @Inject constructor() : Fragment() {

    private val TAG = "CameraFragment"

    private var _binding: FragmentCameraBinding? = null

    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: CameraViewModel by activityViewModels()


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

            val data = viewModel.pickerSavePhoto(savedUri)
            Log.e(TAG, data.toString())

            Navigation.findNavController(requireActivity(), R.id.fcv_film)
                .navigate(
                    CameraFragmentDirections.actionCameraFragmentToCropFragment(
                        data
                    )
                )
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.callback = this

        val viewFinder = binding.viewFinder
        viewFinder.post {
            viewModel.setUpCamera(viewFinder, viewLifecycleOwner)
        }

        addListener(viewFinder)


    }

    private fun addListener(viewFinder: PreviewView) {
        binding.tbCamera.setNavigationOnClickListener {
            requireActivity().finish()
        }
        binding.cameraToAlbumBtn.setOnClickListener { goToAlbum() }
        binding.toggleScreenBtn.setOnClickListener { viewModel.toggleCamera(viewFinder, viewLifecycleOwner) }
    }


    ////////////////
    private fun goToAlbum() {
        Log.e(TAG, "go to album")

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }



}