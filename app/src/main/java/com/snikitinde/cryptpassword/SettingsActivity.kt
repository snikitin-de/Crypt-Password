package com.snikitinde.cryptpassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.content.SharedPreferences
import android.util.Log
import androidx.biometric.BiometricManager


class SettingsActivity : AppCompatActivity() {

    var biometric: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
                             SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }


        override fun onResume() {
            super.onResume()

            preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()

            preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            val setting_biometric = sharedPreferences.getBoolean("setting_biometric",
                                                                 false)

            var activity = SettingsActivity()

            if (setting_biometric){
                activity.biometric = true
            } else if(!setting_biometric) {
                activity.biometric = false
            }
        }
    }
}