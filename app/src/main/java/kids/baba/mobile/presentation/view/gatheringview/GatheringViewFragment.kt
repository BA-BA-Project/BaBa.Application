package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGatheringViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.initAlbum()
        setViewPager()
        collectEvent()
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
    }
}