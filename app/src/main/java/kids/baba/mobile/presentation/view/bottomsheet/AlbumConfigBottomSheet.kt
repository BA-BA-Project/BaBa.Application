package kids.baba.mobile.presentation.view.bottomsheet

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.BottomSheetAlbumConfigBinding
import kids.baba.mobile.presentation.event.AlbumConfigEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AlbumConfigViewModel

@AndroidEntryPoint
class AlbumConfigBottomSheet(private val dismissListener: (AlbumConfigEvent) -> Unit) :
    BottomSheetDialogFragment() {
    private var _binding: BottomSheetAlbumConfigBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AlbumConfigViewModel by viewModels()

    private val deleteDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.delete_album_answer)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.albumDelete()
            }
            .setNegativeButton(
                R.string.cancle
            ) { dialog, _ -> dialog?.cancel() }
            .setCancelable(false)
            .create()
    }


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
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AlbumConfigEvent.ShowDeleteCheckDialog -> {
                        deleteDialog.show()
                    }

                    is AlbumConfigEvent.DeleteAlbum -> {
                        dismissListener(event)
                        dismiss()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
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