package hu.zsof.restaurantapp.util.extensions

import android.widget.EditText
import hu.zsof.restaurantapp.R

fun EditText.validateNonEmpty(): Boolean {
    if (text.isEmpty()) {
        error = context.getString(R.string.require_error)
        return false
    }
    return true
}