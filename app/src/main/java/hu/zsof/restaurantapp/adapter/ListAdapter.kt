package hu.zsof.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.ListItemBinding
import hu.zsof.restaurantapp.network.enums.Price
import hu.zsof.restaurantapp.network.model.Place
import hu.zsof.restaurantapp.ui.list.ListFragmentDirections
import hu.zsof.restaurantapp.util.listeners.FavBtnClickListener
import javax.inject.Inject

class ListAdapter @Inject constructor(
    private val favBtnListener: FavBtnClickListener,
    private val favList: List<Long>
) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>(), Filterable {

    var restaurantList: List<Place>
        get() = differ.currentList.sortedBy { it.name.lowercase() }
        set(value) {
            differ.submitList(value)
        }

    var fixList: MutableList<Place>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding: ListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        return holder.bind(restaurantList[position])
    }

    override fun getItemId(position: Int): Long {
        return restaurantList[position].id
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = restaurantList.size

    private val diffCallback = object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.nameListText.text = place.name
            binding.addressListText.text = place.address
            binding.rateListText.text = place.rate.toString()

            binding.priceListText.text = when (place.price) {
                Price.LOW -> {
                    "$"
                }
                Price.MIDDLE -> {
                    "$$"
                }
                else -> "$$$"
            }

            binding.favIconCheckBox.isChecked = favList.contains(place.id)
            if (binding.favIconCheckBox.isChecked) {
                binding.favIconCheckBox.setButtonDrawable(R.drawable.ic_favs)
            } else {
                binding.favIconCheckBox.setButtonDrawable(R.drawable.ic_fav_outlined)
            }

            binding.favIconCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    favBtnListener.onFavBtnClicked(place.id)
                    binding.favIconCheckBox.setButtonDrawable(R.drawable.ic_favs)
                } else {
                    favBtnListener.onFavBtnClicked(place.id)
                    binding.favIconCheckBox.setButtonDrawable(R.drawable.ic_fav_outlined)
                }
            }

            Glide.with(binding.imageList)
                .load(place.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(
                    binding.imageList
                )

            // todo most nem a viewpager-re visz
            itemView.setOnClickListener {
                println("place adapter: $place")
                val action =
                    ListFragmentDirections.actionListFrToDetailsFr(place = place)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getFilter(): Filter {
        var filterList: MutableList<Place>
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val searchString = charSequence.toString()

                if (fixList == null) {
                    fixList = restaurantList.toMutableList()
                }

                if (searchString.isEmpty()) {
                    filterList = fixList!!
                } else {
                    val tempList: MutableList<Place> = mutableListOf()

                    restaurantList
                        .filter {
                            it.name.lowercase().contains(searchString.lowercase())
                        }
                        .forEach { tempList.add(it) }

                    filterList = tempList
                }

                return FilterResults().apply { values = filterList }
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                filterList = filterResults?.values as MutableList<Place>
                differ.submitList(filterList)
            }
        }
    }
}
