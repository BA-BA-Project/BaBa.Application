package kids.baba.mobile.presentation.view.gatheringview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewAllPagerAdapter(fragmentmanager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentmanager, lifecycle) {

    private val fragmentList = listOf(
        MonthAlbumFragment(), YearAlbumFragment(), AllAlbumFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}