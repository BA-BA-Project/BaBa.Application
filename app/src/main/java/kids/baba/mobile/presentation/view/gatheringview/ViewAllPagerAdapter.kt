package kids.baba.mobile.presentation.view.gatheringview

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewAllPagerAdapter(
    fragment: Fragment,
    private val fragmentArray: Array<String>
) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = fragmentArray.size

    override fun createFragment(position: Int): Fragment {
        return when (fragmentArray[position]) {
            MONTH_CATEGORY -> MonthAlbumFragment()
            YEAR_CATEGORY -> YearAlbumFragment()
            else -> AllAlbumFragment()
        }
    }

    companion object {
        const val MONTH_CATEGORY = "월별"
        const val YEAR_CATEGORY = "년도별"
        const val ALL_CATEGORY = "전체"

    }

}