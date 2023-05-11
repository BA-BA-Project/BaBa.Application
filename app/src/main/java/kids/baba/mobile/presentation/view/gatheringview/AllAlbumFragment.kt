package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAllAlbumBinding
import kids.baba.mobile.presentation.adapter.AllAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.view.dialog.AlbumDetailDialog
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel

@AndroidEntryPoint
class AllAlbumFragment : Fragment() {

    private var _binding: FragmentAllAlbumBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: GatheringAlbumViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var adapter: AllAlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initAdapter()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.allAlbumListState.collect {
                adapter.submitList(it.reversed())
            }
        }
    }

    private fun initAdapter() {
        adapter = AllAlbumAdapter(
            albumClick = { album ->
                showAlbumDialog(album)
            }
        )
        binding.rvAllBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvAllBabies.layoutManager = manager
    }

    private fun showAlbumDialog(album: AlbumUiModel) {
        val albumDetailDialog = AlbumDetailDialog()
        val bundle = Bundle()
        bundle.putParcelable(SELECTED_ALBUM_KEY, album)
        albumDetailDialog.arguments = bundle
        albumDetailDialog.show(childFragmentManager, AlbumDetailDialog.TAG)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val SELECTED_ALBUM_KEY = "SELECTED_ALBUM_KEY"
    }
}