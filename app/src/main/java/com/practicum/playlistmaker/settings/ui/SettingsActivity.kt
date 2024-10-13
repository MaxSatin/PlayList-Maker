package com.practicum.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSettingsBack.setOnClickListener {
            finish()
        }

        viewModel.getIsDarkThemeOnLiveData().observe(this) { isDarkThemeOn ->
            binding.themeSwither.isChecked = isDarkThemeOn
        }

        binding.themeSwither.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.buttonSettingsShare.setOnClickListener {
            viewModel.share(getString(R.string.ShareAppText))
        }

        binding.buttonSettingsSupport.setOnClickListener {
            viewModel.callSupport(
                arrayOf(getString(R.string.practicum_support_mail)),
                getString(R.string.SupportTextHeader),
                getString(R.string.SupportTextBody),
            )
        }

        binding.buttonSettingsUserAgreement.setOnClickListener {
            viewModel.showUserAgreement(getString(R.string.Offer))

        }
    }

}