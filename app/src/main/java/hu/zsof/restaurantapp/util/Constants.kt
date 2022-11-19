package hu.zsof.restaurantapp.util

object Constants {

    const val BASE_URL = "http://wildfire.ddns.net:8092"

    // Length: 6-24 char, at least 1 Uppercase, 1 Number and 1 Symbol
    const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{6,24}$"
    const val EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"

    object Settings {
        const val LAYOUT_FORMAT = "layout_format"
    }

    const val PLACE = "place"
    const val LATLNG = "latlng"
    const val LISTENER = "listener"
}
