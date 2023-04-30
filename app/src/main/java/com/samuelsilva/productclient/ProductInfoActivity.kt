package com.samuelsilva.productclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samuelsilva.productclient.databinding.ActivityProductInfoBinding
import com.samuelsilva.productclient.databinding.ActivityRegisterBinding

class ProductInfoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageGarbage.setOnClickListener(this)
        binding.imageBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            // Abre a tela de cadastro, mas no modo de edição
            startActivity(Intent(this, RegisterActivity::class.java))

        } else if (v.id == binding.imageGarbage.id) {
            // Abre a camera para ler o QRCode

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}