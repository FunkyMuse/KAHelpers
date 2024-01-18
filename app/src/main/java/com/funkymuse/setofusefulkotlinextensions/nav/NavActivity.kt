package com.funkymuse.setofusefulkotlinextensions.nav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.funkymuse.setofusefulkotlinextensions.databinding.ActivityNavBinding
import com.funkymuse.viewbinding.viewBinding


class NavActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNavBinding::inflate)
    private val currentNavController get() = binding.fragmentContainer.getFragment<NavHostFragment>().navController

    override fun onSupportNavigateUp() = currentNavController.navigateUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}