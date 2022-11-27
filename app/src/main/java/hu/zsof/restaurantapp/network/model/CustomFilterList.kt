package hu.zsof.restaurantapp.network.model

data class CustomFilterList(
    val filters: MutableList<Boolean> = mutableListOf()
) {
    // Csak akkor ad vissza igazat, ha adott elem mindkét listában igaz
    fun compare(compareTo: CustomFilterList): Boolean {
        if (this.filters.size == compareTo.filters.size) {
            for (i in 0 until filters.size) {
                if (this.filters[i]) {
                    if (!compareTo.filters[i]) {
                        return false
                    }
                }
            }
            return true
        } else {
            return false
        }
    }
}
