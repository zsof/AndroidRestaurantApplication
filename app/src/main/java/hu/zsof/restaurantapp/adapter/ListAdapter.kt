package hu.zsof.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.databinding.ListItemBinding
import hu.zsof.restaurantapp.model.PlaceData
import javax.inject.Inject

class ListAdapter @Inject constructor() :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>(), Filterable {

    var restaurantList: List<PlaceData>
        get() = differ.currentList.sortedBy { it.name.lowercase() }
        set(value) {
            differ.submitList(value)
        }

    var fixList: MutableList<PlaceData>? = null


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

    private val diffCallback = object : DiffUtil.ItemCallback<PlaceData>() {
        override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: PlaceData) {
            binding.nameListText.text = restaurant.name
            binding.addressListText.text = restaurant.address
            binding.rateListText.text = restaurant.rate.toString()

           /* itemView.setOnClickListener {
                *//*val action =
                    AddressListFragmentDirections.actionAddressListToTaskList(
                        address.convertToAddress(),
                        agent
                    )*//*
                itemView.findNavController().navigate(action)
            }*/
        }
    }

    override fun getFilter(): Filter {
        var filterList: MutableList<PlaceData>
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val searchString = charSequence.toString()

                if (fixList == null) {
                    fixList = restaurantList.toMutableList()
                }

                if (searchString.isEmpty()) {
                    filterList = fixList!!
                } else {
                    val tempList: MutableList<PlaceData> = mutableListOf()

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
                filterList = filterResults?.values as MutableList<PlaceData>
                differ.submitList(filterList)
            }
        }
    }
}