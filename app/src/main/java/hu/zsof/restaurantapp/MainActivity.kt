package hu.zsof.restaurantapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import dagger.hilt.android.AndroidEntryPoint
import hu.zsof.restaurantapp.databinding.ActivityMainBinding
import hu.zsof.restaurantapp.util.Constants

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavController()
        setupPreferences()
    }

    private fun setupNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment
            ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.slide_up)
            .setPopEnterAnim(R.anim.slide_down)
            .build()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.listFragment -> {
                    findNavControllerCustom()?.navigate(R.id.listFragment, null, options)
                    return@setOnItemSelectedListener true
                }
                R.id.mapFragment -> {
                    findNavControllerCustom()?.navigate(R.id.mapFragment, null, options)
                    return@setOnItemSelectedListener true
                }
                R.id.favListFragment -> {
                    findNavControllerCustom()?.navigate(R.id.favListFragment, null, options)
                    return@setOnItemSelectedListener true
                }
                R.id.userProfileFragment -> {
                    findNavControllerCustom()?.navigate(R.id.userProfileFragment, null, options)
                    return@setOnItemSelectedListener true
                }
                R.id.loginFragment -> {
                    findNavControllerCustom()?.navigate(R.id.loginFragment, null, options)
                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }
    }

    private fun findNavControllerCustom(): NavController? {
        val navHostFragment =
            (this as? MainActivity)?.supportFragmentManager?.findFragmentById(
                R.id.nav_host_fragment_container,
            ) as? NavHostFragment
        return navHostFragment?.navController
    }

    private fun setupPreferences() {
        // Dark mode
        val darkModePref = viewModel.getAppPreference<String>(Constants.Prefs.DARK_MODE)
        if (darkModePref == "1") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        /*   // Locale
           val localeToSwitchTo = viewModel.getAppPreference<String>(Constants.Prefs.LOCALE)
           Locale.forLanguageTag(localeToSwitchTo)
           LocaleUtil.updateLocale(this, Locale.forLanguageTag(localeToSwitchTo))
           println("local main ${viewModel.getAppPreference<String>(Constants.Prefs.LOCALE)} $localeToSwitchTo")*/
    }
}
