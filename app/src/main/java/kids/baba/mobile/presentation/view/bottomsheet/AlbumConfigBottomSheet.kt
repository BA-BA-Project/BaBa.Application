package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetAlbumConfigBinding
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AlbumConfigViewModel

@AndroidEntryPoint
class AlbumConfigBottomSheet  : BottomSheetDialogFragment(){
    private var _binding: BottomSheetAlbumConfigBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AlbumConfigViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAlbumConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        collectAlbumState()
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect{ event ->
                when(event) {
                    is AlbumConfigEvent.DialogDismiss -> dismiss()
                    else -> {}
                }
            }
        }
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectAlbumState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.album.collect{
                binding.btnSavePhoto.visibility = if (it.isMyBaby) View.VISIBLE else View.GONE
                binding.btnDeleteAlbum.visibility = if (it.isMyBaby) View.VISIBLE else View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AlbumConfigBottomSheet"
        const val NOW_ALBUM_KEY = "NOW_ALBUM_KEY"
    }
}