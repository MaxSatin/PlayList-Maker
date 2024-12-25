package com.practicum.playlistmaker.main.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.RootActivityBinding

class RootActivity : AppCompatActivity() {

    private var _binding: RootActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = RootActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootContainerView) as NavHostFragment
        val navHostController = navHostFragment.findNavController()

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navHostController)

        navHostController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.playerFragment -> binding.bottomNavigationView.isVisible = false
                else -> binding.bottomNavigationView.isVisible = true
            }

        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()

    }
}



