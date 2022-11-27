package hu.zsof.restaurantapp.network.model

data class CustomFilter(
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
) {
    fun convertToList(): CustomFilterList {
        return CustomFilterList(
            mutableListOf(
                freeParking,
                glutenFree,
                lactoseFree,
                vegetarian,
                vegan,
                fastFood,
                parkingAvailable,
                dogFriendly,
                familyPlace,
                delivery,
                creditCard,
                szepCard
            )
        )
    }
}
