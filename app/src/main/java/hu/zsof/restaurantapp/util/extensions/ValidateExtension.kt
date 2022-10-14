package hu.zsof.restaurantapp.util.extensions

import android.widget.EditText
import hu.zsof.restaurantapp.R
import hu.zsof.restaurantapp.util.Constants.EMAIL_PATTERN
import hu.zsof.restaurantapp.util.Constants.PASSWORD_PATTERN
import java.util.regex.Pattern

fun EditText.validateNonEmptyField(): Boolean {
    if (text.isEmpty()) {
        error = context.getString(R.string.require_error)
        return false
    }
    return true
}

fun EditText.isPasswordValid(): Boolean {
    val pattern = Pattern.compile(PASSWORD_PATTERN)

    if (pattern.matcher(text).matches()) {
        error = context.getString(R.string.password_not_strong_error)
        return false
    }
    return true
}

fun EditText.isEmailValid(): Boolean {
    val pattern = Pattern.compile(EMAIL_PATTERN)

    if (pattern.matcher(text).matches()) {
        error = context.getString(R.string.email_not_valid_error)
        return false
    }
    return true
}
