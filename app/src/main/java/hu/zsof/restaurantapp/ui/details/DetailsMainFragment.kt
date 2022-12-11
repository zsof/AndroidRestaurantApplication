package hu.zsof.restaurantapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.DetailsMainFragmentBinding
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.util.Constants.PLACE

class DetailsMainFragment : Fragment() {
    private lateinit var binding: DetailsMainFragmentBinding
    private val viewModel: DetailsViewModel by viewModels()
    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            place = it.get(PLACE) as Place
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.details_main_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
    }

    private fun setupBindings() {
        binding.apply {
            nameDetailsText.text = place?.name
            addressDetailsText.text = place?.address
            webDetailsText.text = place?.web
            emailDetailsText.text = place?.email
            phoneDetailsText.text = place?.phoneNumber
            Glide.with(requireContext())
                .load(place?.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(
                    imageDetails
                )

            println("monday")
            mondayOpen.text = if (place?.openDetails?.mondayOpen != getString(R.string.set_time)) {
                place?.openDetails?.mondayOpen
            } else getString(
                R.string.not_set
            )
            mondayClose.text =
                if (place?.openDetails?.mondayClose != getString(R.string.set_time)) {
                    place?.openDetails?.mondayClose
                } else getString(
                    R.string.not_set
                )
            tuesdayOpen.text =
                if (place?.openDetails?.tuesdayOpen != getString(R.string.set_time)) {
                    place?.openDetails?.tuesdayOpen
                } else getString(
                    R.string.not_set
                )
            tuesdayClose.text =
                if (place?.openDetails?.tuesdayClose != getString(R.string.set_time)) {
                    place?.openDetails?.tuesdayClose
                } else getString(
                    R.string.not_set
                )
            wednesdayOpen.text =
                if (place?.openDetails?.wednesdayOpen != getString(R.string.set_time)) {
                    place?.openDetails?.wednesdayOpen
                } else getString(
                    R.string.not_set
                )
            wednesdayClose.text =
                if (place?.openDetails?.wednesdayClose != getString(R.string.set_time)) {
                    place?.openDetails?.wednesdayClose
                } else getString(
                    R.string.not_set
                )
            thursdayOpen.text =
                if (place?.openDetails?.thursdayOpen != getString(R.string.set_time)) {
                    place?.openDetails?.thursdayOpen
                } else getString(
                    R.string.not_set
                )
            thursdayClose.text =
                if (place?.openDetails?.thursdayClose != getString(R.string.set_time)) {
                    place?.openDetails?.thursdayClose
                } else getString(
                    R.string.not_set
                )
            fridayOpen.text = if (place?.openDetails?.fridayOpen != getString(R.string.set_time)) {
                place?.openDetails?.fridayOpen
            } else getString(
                R.string.not_set
            )
            fridayClose.text =
                if (place?.openDetails?.fridayClose != getString(R.string.set_time)) {
                    place?.openDetails?.fridayClose
                } else getString(
                    R.string.not_set
                )
            saturdayOpen.text =
                if (place?.openDetails?.saturdayOpen != getString(R.string.set_time)) {
                    place?.openDetails?.saturdayOpen
                } else getString(
                    R.string.not_set
                )
            saturdayClose.text =
                if (place?.openDetails?.saturdayClose != getString(R.string.set_time)) {
                    place?.openDetails?.saturdayClose
                } else getString(
                    R.string.not_set
                )
            sundayOpen.text = if (place?.openDetails?.sundayOpen != getString(R.string.set_time)) {
                place?.openDetails?.sundayOpen
            } else getString(
                R.string.not_set
            )
            sundayClose.text =
                if (place?.openDetails?.sundayClose != getString(R.string.set_time)) {
                    place?.openDetails?.sundayClose
                } else getString(
                    R.string.not_set
                )

            expandIcon.setOnClickListener {
                if (cardGroup.visibility == View.VISIBLE) {
                    expandIcon.setImageResource(R.drawable.ic_expand)
                    cardGroup.visibility = View.GONE
                } else {
                    expandIcon.setImageResource(R.drawable.ic_collapse)
                    cardGroup.visibility = View.VISIBLE
                }
            }
        }
    }
}
