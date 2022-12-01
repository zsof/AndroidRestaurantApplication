package hu.zsof.restaurantapp.util.extensions

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.zsof.restaurantapp.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreference @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(Constants.Prefs.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    @Suppress("UNCHECKED_CAST")
    fun <T> getPreference(key: String, defaultValue: T? = null): T {
        return when (defaultValue) {
            is Int -> sharedPreferences.getInt(key, defaultValue) as T
            is Float -> sharedPreferences.getFloat(key, defaultValue) as T
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
            is Long -> sharedPreferences.getLong(key, defaultValue) as T
            else -> sharedPreferences.getString(key, "")!! as T
        }
    }

    fun <T> setPreference(key: String, value: T) {
        sharedPreferences.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                else -> error("Only primitive types can be stored in SharedPreferences")
            }
            apply()
        }
    }
}
