package com.samuelsilva.productclient.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.samuelsilva.productclient.databinding.ActivityMainBinding
import com.samuelsilva.productclient.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageFilter.setOnClickListener(this)
        binding.imageLogout.setOnClickListener(this)

        // Observadores
        observe()
    }

    override fun onResume() {
        super.onResume()
        viewModel.list()
    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            startActivity(Intent(this, RegisterActivity::class.java))

        } else if (v.id == binding.imageFilter.id) {
            startActivity(Intent(this, SearchFilterActivity::class.java))

        } else if (v.id == binding.imageLogout.id) {
            handleLogout()
        }
    }

    private fun observe() {
        viewModel.status.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                if (it.expiredLogin()) {
                    val snackbar = Snackbar.make(binding.root, it.message(), Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("OK") {
                        snackbar.dismiss()
                        // Ação ao clicar no botão "OK"
                        handleLogout()
                    }
                    snackbar.show()

                } else {
                    Snackbar.make(binding.root, it.message(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleLogout() {
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}