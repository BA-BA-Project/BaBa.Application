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
import kids.baba.mobile.databinding.FragmentClassifiedAlbumDetailBinding
import kids.baba.mobile.presentation.adapter.AllAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.ClassifiedAlbumDetailViewModel
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel

@AndroidEntryPoint
class ClassifiedAlbumDetailFragment : Fragment() {
    private var _binding: FragmentClassifiedAlbumDetailBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }


    // requireParentFragment?
    val viewModel: GatheringAlbumViewModel by viewModels(
        ownerProducer = {requireParentFragment()}
    )

    val viewModel2: ClassifiedAlbumDetailViewModel by viewModels()


    private lateinit var adapter: AllAlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassifiedAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = AllAlbumAdapter()
        binding.rvClassifiedAllBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.rvClassifiedAllBabies.layoutManager = manager

        viewLifecycleOwner.repeatOnStarted {
            viewModel.monthAlbumListState.collect {
                Log.e("ClassifiedAlbumDetailFrag", "itemId: ${viewModel2.itemId}")
                adapter.submitList(it[viewModel2.itemId])
            }
        }


    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}