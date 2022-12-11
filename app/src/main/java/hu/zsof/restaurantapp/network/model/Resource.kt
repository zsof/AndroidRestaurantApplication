package hu.zsof.restaurantapp.network.model

data class Resource(
    val imageId: Long,
    var image: String? = null,
    val typeId: Long,
    var type: String = ""
)
