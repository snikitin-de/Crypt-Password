package com.snikitinde.cryptpassword

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.glxn.qrgen.android.QRCode
import android.view.MenuItem
import android.view.Menu
import androidx.biometric.BiometricPrompt
import androidx.preference.PreferenceManager
import androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

    private var biometricPrompt: BiometricPrompt? = null
    private val executor: Executor = MainThreadExecutor()

    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                biometricPrompt!!.cancelAuthentication()
                finish()
            toast(errString.toString())
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            toast(getString(R.string.setting_biometric_on_auth_succeed))
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            toast(getString(R.string.setting_biometric_on_auth_failed))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val settingBiometric = preferences.getBoolean("setting_biometric", false)

        if(settingBiometric){
            if (biometricPrompt == null)
                biometricPrompt = BiometricPrompt(this, executor, callback)

            val promptInfo: BiometricPrompt.PromptInfo = buildBiometricPrompt()
            biometricPrompt!!.authenticate(promptInfo)
        }

        btnEncrypt.setOnClickListener {
            if (etText.text.isBlank()) {
                toast(getString(R.string.text_empty))
            }
            if (etPassword.text.isBlank()) {
                toast(getString(R.string.password_empty))
            }
            if (etCodeword.text.isBlank()) {
                toast(getString(R.string.codeword_empty))
            }
            if (!etText.text.isBlank() &&
                !etPassword.text.isBlank() &&
                !etCodeword.text.isBlank()) {

                val encryptedString = EncryptString().fixedSaltEncryptString(
                                                        etText.text.toString(),
                                                        etPassword.text.toString(),
                                                        etCodeword.text.toString(),
                                                        "SHA-512"
                                                   )
                txtViewEncryptedString.text = encryptedString

                val bitmap = QRCode.from(encryptedString).withSize(1000, 1000).bitmap()
                imgViewQR.setImageBitmap(bitmap)
                imgViewQR.visibility = View.VISIBLE
            }
        }

        btnCopy.setOnClickListener {
            if (txtViewEncryptedString.text != getString(R.string.txtViewEncryptedString)){
                copyToClip(
                    getString(R.string.app_name),
                    txtViewEncryptedString.text.toString(),
                    getString(R.string.copy_success)
                )
            } else {
                toast(getString(R.string.copy_fail))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun copyToClip(label: String, text: String, toast: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(label, text)

        clipboard.setPrimaryClip(clip)
        toast(toast)
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun buildBiometricPrompt(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.setting_biometric_authentication))
            .setSubtitle(getString(R.string.setting_biometric_authentication_subtitle))
            .setDescription(getString(R.string.setting_biometric_authentication_description))
            .setNegativeButtonText(
                getString(R.string.setting_biometric_authentication_negative_button)
            )
            .build()
    }
}
