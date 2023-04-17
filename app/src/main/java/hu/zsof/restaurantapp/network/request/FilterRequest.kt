package hu.zsof.restaurantapp.network.request

import hu.zsof.restaurantapp.network.enums.Type
import hu.zsof.restaurantapp.network.model.CustomFilter

data class FilterRequest(
    var filter: CustomFilter = CustomFilter(),
    var type: Type = Type.RESTAURANT
    /* val rate: Float? = 0.0f,
     val price: Price = Price.LOW*/
)
