package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentClassifiedAlbumBinding
import kids.baba.mobile.presentation.adapter.MonthAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel

@AndroidEntryPoint
class MonthAlbumFragment : Fragment() {

    private var _binding: FragmentClassifiedAlbumBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: GatheringAlbumViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var adapter: MonthAlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassifiedAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initAdapter()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.recentMonthAlbumListState.collect {
                adapter.submitList(it)
            }
        }

    }

    private fun initAdapter() {
        adapter = MonthAlbumAdapter(
            albumClick = {baby, itemId ->
                viewModel.showClassifiedDetailAlbums(baby, itemId)

            }
        )
        binding.rvMonthBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMonthBabies.layoutManager = manager
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}