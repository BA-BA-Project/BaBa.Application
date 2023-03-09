package kids.baba.mobile.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGrowthAlbumBinding
import kids.baba.mobile.presentation.helper.CameraPermissionRequester
import kids.baba.mobile.presentation.view.film.CameraActivity

//@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {

    private var _binding: FragmentGrowthAlbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    // connect is the callback function on permission granted
    // connect 는 permission 허용의 콜백 함수
    // noPermission 은 permission 거부 시 콜백
    private val permissionRequester = CameraPermissionRequester(this, ::connect, ::noPermission)

    private fun noPermission() {
        // Whatever you need to do if the permission has been denied
        Log.d("GrowthAlbumFragment", "noPermission()")

    }

    private fun connect() {
        // Whatever call to connect using bluetooth, once here the permission is granted
        // 블루투스와 연결된 무엇이든지 호출하면,
        Log.d("GrowthAlbumFragment", "connect()")
        startCameraActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrowthAlbumBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testBtn.setOnClickListener {
            permissionRequester.checkPermissions(requireContext())
        }

    }

    private fun startCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        requireContext().startActivity(intent)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}