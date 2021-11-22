package com.crazylegend.setofusefulkotlinextensions.nav

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityNavBinding
import com.crazylegend.viewbinding.viewBinding

/**
 * Created by Hristijan, date 2/15/21
 */
class NavActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNavBinding::inflate)
    private val currentNavController get() = binding.fragmentContainer.getFragment<NavHostFragment>().navController

    override fun onSupportNavigateUp() = currentNavController.navigateUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}