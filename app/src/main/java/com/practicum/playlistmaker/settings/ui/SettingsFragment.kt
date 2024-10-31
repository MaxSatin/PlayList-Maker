package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SettingsFragmentBinding
import com.practicum.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIsDarkThemeOnLiveData().observe(viewLifecycleOwner) { isDarkThemeOn ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
