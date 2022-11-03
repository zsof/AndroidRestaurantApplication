package hu.zsof.restaurantapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.ui.details.DetailsMainFragment

class DetailsViewPagerAdapter(fragment: Fragment, private val context: Context) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailsMainFragment()
            else -> throw IllegalStateException("There is no fragment with this position: $position")
        }
    }

    fun getTitle(position: Int): String {
        return when (position) {
            0 -> context.getString(R.string.details_tab_text)
            else -> throw IllegalStateException("There is no fragment with this position: $position")
        }
    }
}
