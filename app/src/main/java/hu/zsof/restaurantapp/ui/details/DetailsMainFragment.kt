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
            println("place details $place")
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
        }
    }
}
