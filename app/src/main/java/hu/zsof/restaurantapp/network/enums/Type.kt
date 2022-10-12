package hu.zsof.restaurantapp.network.enums

enum class Type {
    RESTAURANT, CAFE, PATISSERIE, BAKERY;

    companion object {

        fun getByOrdinal(ordinal: Int): Type {
            var type: Type = RESTAURANT
            for (t in values()) {
                if (t.ordinal == ordinal) {
                    type = t
                    break
                }
            }
            return type
        }
    }
}