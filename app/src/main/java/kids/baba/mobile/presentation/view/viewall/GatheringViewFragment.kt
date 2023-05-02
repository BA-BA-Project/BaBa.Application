package kids.baba.mobile.presentation.view.viewall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentGatheringViewBinding

@AndroidEntryPoint
class GatheringViewFragment : Fragment() {
    private var _binding: FragmentGatheringViewBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private lateinit var pagerAdapter: ViewAllPagerAdapter


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
        setViewPager()
    }

    private fun setViewPager() {
        pagerAdapter = ViewAllPagerAdapter(this)
        binding.vpViewAll.adapter = pagerAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}