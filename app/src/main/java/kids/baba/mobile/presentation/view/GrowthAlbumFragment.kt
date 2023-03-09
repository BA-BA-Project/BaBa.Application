package kids.baba.mobile.presentation.view

import android.content.Intent
import android.graphics.Camera
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGrowthAlbumBinding
import kids.baba.mobile.presentation.view.film.CameraActivity

@AndroidEntryPoint
class GrowthAlbumFragment : Fragment() {

    private var _binding: FragmentGrowthAlbumBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }


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
            startCameraActivity()
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