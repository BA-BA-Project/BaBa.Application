package kids.baba.mobile.presentation.view.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewpagerFragmentAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val fragmentList = listOf(
        FirstOnBoarding(), SecondOnBoarding(), ThirdOnBoarding()
    )

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}