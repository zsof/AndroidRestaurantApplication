package hu.zsof.restaurantapp.network.model

data class Filter(
    var freeParking: Boolean = false,
    var glutenFree: Boolean = false,
    var lactoseFree: Boolean = false,
    var vegetarian: Boolean = false,
    var vegan: Boolean = false,
    var fastFood: Boolean = false,

    var parkingAvailable: Boolean = false,
    var dogFriendly: Boolean = false,
    var familyPlace: Boolean = false,
    var delivery: Boolean = false,
    var creditCard: Boolean = false,
    var szepCard: Boolean = false
)
