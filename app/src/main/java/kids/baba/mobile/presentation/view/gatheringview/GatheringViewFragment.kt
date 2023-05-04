package kids.baba.mobile.presentation.view.gatheringview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGatheringViewBinding
import kids.baba.mobile.presentation.viewmodel.GatheringAlbumViewModel
import javax.inject.Inject

@AndroidEntryPoint
class GatheringViewFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentGatheringViewBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val tabTitleArray = arrayOf("월별", "년도별", "전체")

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
        binding.lifecycleOwner = this

        setViewPager()
    }

    private fun setViewPager() {
        val viewPager = binding.vpViewAll
        val tabLayout = binding.tlGatheringView
        viewPager.adapter = ViewAllPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}