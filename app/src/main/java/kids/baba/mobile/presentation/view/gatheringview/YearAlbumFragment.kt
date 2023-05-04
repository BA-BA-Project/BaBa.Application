package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentClassifiedAlbumBinding
import kids.baba.mobile.presentation.adapter.YearAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel
import javax.inject.Inject

@AndroidEntryPoint
class YearAlbumFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentClassifiedAlbumBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: GatheringAlbumViewModel by viewModels()

    private lateinit var adapter: YearAlbumAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassifiedAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initAdapter()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.recentYearAlbumListState.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun initAdapter() {
        adapter = YearAlbumAdapter()
        binding.rvMonthBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMonthBabies.layoutManager = manager
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}