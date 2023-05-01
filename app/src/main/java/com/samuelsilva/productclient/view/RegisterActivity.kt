package com.samuelsilva.productclient.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samuelsilva.productclient.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageQrCode.setOnClickListener(this)
        binding.imageMinus.setOnClickListener(this)
        binding.imagePlus.setOnClickListener(this)
        binding.imageBack.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            // Salva o produto

        } else if (v.id == binding.imageQrCode.id) {
            // Abre a camera para ler o QRCode

        } else if (v.id == binding.imageMinus.id) {
            // Faz algo

        } else if (v.id == binding.imagePlus.id) {
            // Faz algo

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}