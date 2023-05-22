package com.samuelsilva.productclient.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.databinding.ActivityLoginBinding
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.viewmodel.LoginViewModel
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        // Layout
        setContentView(binding.root)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        ProductModel.productFilter = ProductModel().apply {
            id = null
            name = null
            description = null
            category = null
            productBrand = null
            provider = null
            quantity = null
            barCode = null
            fabricationDate = null
            expirationDate = null
            inclusionDate = null
        }

        // Eventos de click
        binding.buttonLogin.setOnClickListener(this)

        // Verifica se o usuário já está logado
        viewModel.verifyAuthentication()

        // Observadores
        observe()

    }

    override fun onClick(v: View) {
        if (v.id == binding.buttonLogin.id) {
            hideKeyboard(v)
            handleLogin()
        }
    }

    private fun hideKeyboard(v: View) {
        // Código para esconder o teclado quando apertar o botão
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        viewModel.doLogin(email, password)
    }

    private fun observe() {
        viewModel.login.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(binding.root, it.message(), Snackbar.LENGTH_LONG).show()
            }

        }

        viewModel.loggedUser.observe(this) {
            if (it) {
                biometricAuthetication()
            }
        }
    }

    private fun biometricAuthetication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            })

        // Informações apresentadas no momento da autenticação
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.title_biometric_authetication))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        // Exibe para o usuário
        biometricPrompt.authenticate(info)
    }
}