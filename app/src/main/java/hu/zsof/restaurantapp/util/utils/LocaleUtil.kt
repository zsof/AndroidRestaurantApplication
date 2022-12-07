package hu.zsof.restaurantapp.util.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

object LocaleUtil {
    fun updateLocale(context: Context, localeToSwitchTo: Locale) {
        val resources = context.resources
        val configuration: Configuration = resources.configuration
        // val locale: Locale = Locale.forLanguageTag(localeToSwitchTo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.setLocale(localeToSwitchTo)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(configuration)
        } else {
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }
}
