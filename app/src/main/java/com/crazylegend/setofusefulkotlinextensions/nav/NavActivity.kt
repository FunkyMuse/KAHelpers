package com.crazylegend.setofusefulkotlinextensions.nav

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.crazylegend.navigation.setupWithNavController
import com.crazylegend.setofusefulkotlinextensions.R
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityNavBinding
import com.crazylegend.viewbinding.viewBinder

/**
 * Created by Hristijan, date 2/15/21
 */
class NavActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null

    private val binding by viewBinder(ActivityNavBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    @SuppressLint("ResourceType")
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
                R.navigation.test01,
                R.navigation.test02,
                R.navigation.test03,
                R.navigation.test04,
                R.navigation.test05)
        // Setup the bottom navigation view with a list of navigation graphs

        val controller = binding.bottomNav.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.fragmentContainer,
                intent = intent,
                R.anim.nav_default_enter_anim,
                R.anim.nav_default_exit_anim,
                R.anim.nav_default_pop_enter_anim,
                R.anim.nav_default_pop_exit_anim
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            listenToDestinationChanges(navController)
        }
        currentNavController = controller

    }

    private fun listenToDestinationChanges(navController: NavController?) {
        navController ?: return

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    override fun onSupportNavigateUp() = currentNavController?.value?.navigateUp() ?: false
}