package hu.zsof.restaurantapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.ui.details.DetailsMainFragment
import hu.zsof.restaurantapp.ui.details.DetailsOtherFragment

class DetailsViewPagerAdapter(fragment: Fragment, private val context: Context) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailsMainFragment()
            1 -> DetailsOtherFragment()
            else -> throw IllegalStateException("There is no fragment with this position: $position")
        }
    }

    fun getTitle(position: Int): String {
        return when (position) {
            0 -> context.getString(R.string.details_tab_text)
            1 -> "Other"
            else -> throw IllegalStateException("There is no fragment with this position: $position")
        }
    }
}
