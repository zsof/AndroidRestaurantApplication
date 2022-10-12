package hu.zsof.restaurantapp.network.enums

enum class Price {
    LOW,
    MIDDLE,
    HIGH;

    companion object {

        fun getByOrdinal(ordinal: Int): Price {
            var price: Price = LOW
            for (p in values()) {
                if (p.ordinal == ordinal) {
                    price = p
                    break
                }
            }
            return price
        }
    }
}
