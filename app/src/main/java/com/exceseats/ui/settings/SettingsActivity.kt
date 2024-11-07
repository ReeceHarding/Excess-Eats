package com.exceseats.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.exceseats.databinding.ActivitySettingsBinding
import com.exceseats.ui.auth.LoginActivity
import com.exceseats.util.Constants
import com.exceseats.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.notificationRadiusSeekBar.apply {
            max = Constants.MAX_NOTIFICATION_RADIUS
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.notificationRadiusValueTextView.text =
                        getString(R.string.notification_radius_value, progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val radius = binding.notificationRadiusSeekBar.progress

            when {
                name.isBlank() -> {
                    showToast("Please enter your name")
                }
                else -> {
                    viewModel.updateProfile(name, radius)
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun observeViewModel() {
        viewModel.settingsState.observe(this) { state ->
            binding.progressBar.isVisible = state is SettingsState.Loading
            binding.contentGroup.isVisible = state is SettingsState.Success

            when (state) {
                is SettingsState.Success -> {
                    binding.apply {
                        nameEditText.setText(state.user.name)

                        val radius = state.user.preferences["notificationRadius"] as? Int
                            ?: Constants.DEFAULT_NOTIFICATION_RADIUS
                        notificationRadiusSeekBar.progress = radius
                        notificationRadiusValueTextView.text =
                            getString(R.string.notification_radius_value, radius)
                    }
                }
                is SettingsState.Error -> {
                    showToast(state.message)
                }
                is SettingsState.LoggedOut -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                else -> Unit
            }
        }
    }
}
