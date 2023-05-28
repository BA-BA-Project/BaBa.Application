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
import kids.baba.mobile.databinding.FragmentClassifiedAlbumBinding
import kids.baba.mobile.presentation.adapter.YearAlbumAdapter
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel

@AndroidEntryPoint
class YearAlbumFragment : Fragment() {

    private var _binding: FragmentClassifiedAlbumBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: GatheringAlbumViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private lateinit var adapter: YearAlbumAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClassifiedAlbumBinding.inflate(inflater, container, false)
        Log.i(TAG, "onCreateView()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        Log.i(TAG, "onViewCreated()")

        initAdapter()

        Log.d("ViewModelTestY", viewModel.toString())


        Log.d("YearFragment value", viewModel.recentYearAlbumListState.value.toString())
        viewLifecycleOwner.repeatOnStarted {
            viewModel.recentYearAlbumListState.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun initAdapter() {
        adapter = YearAlbumAdapter(
            albumClick = { itemId ->
                viewModel.showClassifiedAllAlbumsByYear(itemId)
            }
        )
        binding.rvMonthBabies.adapter = adapter
        val manager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMonthBabies.layoutManager = manager
    }

    override fun onDestroyView() {
        _binding = null

        Log.i(TAG, "onDestroyView()")
        super.onDestroyView()
    }
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
    }


    companion object {
        const val TAG = "YearFragment"
    }
}