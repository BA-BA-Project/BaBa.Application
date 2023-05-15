package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGatheringViewBinding
import kids.baba.mobile.presentation.event.GatheringAlbumEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel

@AndroidEntryPoint
class GatheringViewFragment : Fragment() {

    private var _binding: FragmentGatheringViewBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val fragmentArray = arrayOf(
        ViewAllPagerAdapter.MONTH_CATEGORY, ViewAllPagerAdapter.YEAR_CATEGORY, ViewAllPagerAdapter.ALL_CATEGORY
    )

    val viewModel: GatheringAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("GatheringViewFragment","onCreated called. savedInstanceState:Bundle = $savedInstanceState")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGatheringViewBinding.inflate(inflater, container, false)
        Log.e("GatheringViewFragment","onCreateView called. savedInstanceState: Bundle = $savedInstanceState")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.initAlbum()
        setViewPager()
        collectEvent()
        Log.e("GatheringViewFragment","onViewCreated called. savedInstanceState: Bundle = $savedInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.e("GatheringViewFragment","onViewStateRestored called. savedInstanceState: Bundle = $savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.e("GatheringViewFragment","onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.e("GatheringViewFragment","onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.e("GatheringViewFragment","onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.e("GatheringViewFragment","onStop called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("GatheringViewFragment","onSaveInstanceState called outState: Bundle = $outState")
    }

    private fun setViewPager() {
        val viewPager = binding.vpViewAll
        val tabLayout = binding.tlGatheringView
        viewPager.adapter = ViewAllPagerAdapter(this, fragmentArray = fragmentArray)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentArray[position]
        }.attach()
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is GatheringAlbumEvent.Initialized -> Unit
                    is GatheringAlbumEvent.GoToClassifiedAllAlbum -> {
                        val action =
                            GatheringViewFragmentDirections.actionGatheringViewFragmentToClassifiedAlbumDetailFragment(
                                event.itemId, event.classifiedAlbumList, event.fromMonth
                            )
                        findNavController().navigate(action)
                    }
                    is GatheringAlbumEvent.ShowSnackBar -> showSnackBar(event.text)
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        Log.e("GatheringViewFragment","onDestroyView called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("GatheringViewFragment","onDestroy called")
    }
}