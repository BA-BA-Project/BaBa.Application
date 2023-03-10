//package kids.baba.mobile.presentation.view.film
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import dagger.hilt.android.AndroidEntryPoint
//import kids.baba.mobile.databinding.FragmentCameraBinding
//import kids.baba.mobile.presentation.viewmodel.CameraViewModel
//
////@OptIn(ExperimentalPermissionsApi::class)
////@Composable
//@AndroidEntryPoint
//class CameraFragment : Fragment() {
//
//    private var _binding: FragmentCameraBinding? = null
//    private val binding
//        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
//
//    private val viewModel: CameraViewModel by viewModels()
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        _binding = FragmentCameraBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//
//
//    }
//
//
//}