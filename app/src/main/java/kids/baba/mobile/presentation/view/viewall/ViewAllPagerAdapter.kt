package kids.baba.mobile.presentation.view.viewall

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewAllPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {


    private val fragmentList = listOf(
        MonthView(), YearView(), AllView()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}