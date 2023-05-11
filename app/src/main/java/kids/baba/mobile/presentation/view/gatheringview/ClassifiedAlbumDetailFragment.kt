package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentClassifiedAlbumDetailBinding
import kids.baba.mobile.presentation.adapter.AllAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.view.dialog.AlbumDetailDialog
import kids.baba.mobile.presentation.viewmodel.ClassifiedAlbumDetailViewModel

@AndroidEntryPoint
class ClassifiedAlbumDetailFragment : Fragment() {
    private var _binding: FragmentClassifiedAlbumDetailBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: ClassifiedAlbumDetailViewModel by viewModels()

    private lateinit var adapter: AllAlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassifiedAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNavigation()
        initBinding()
        initAdapter()
        collectList()
    }

    private fun initNavigation() {
        binding.tbClassifiedAlbumDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initAdapter() {
        adapter = AllAlbumAdapter(
            albumClick = { album ->
                showAlbumDialog(album)
            }
        )
        binding.rvClassifiedAllBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvClassifiedAllBabies.layoutManager = manager
    }

    private fun showAlbumDialog(album: AlbumUiModel) {
        val albumDetailDialog = AlbumDetailDialog()
        val bundle = Bundle()
        bundle.putParcelable(SELECTED_ALBUM_KEY, album)
        albumDetailDialog.arguments = bundle
        albumDetailDialog.show(childFragmentManager, AlbumDetailDialog.TAG)
    }

    private fun collectList() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.albumList.collect {
                adapter.submitList(it.classifiedAlbumList)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val SELECTED_ALBUM_KEY = "SELECTED_ALBUM_KEY"
    }
}