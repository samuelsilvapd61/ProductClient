package com.samuelsilva.productclient.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samuelsilva.productclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageFilter.setOnClickListener(this)
        binding.imageLogout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            startActivity(Intent(this, RegisterActivity::class.java))

        } else if (v.id == binding.imageFilter.id) {
            startActivity(Intent(this, SearchFilterActivity::class.java))

        } else if (v.id == binding.imageLogout.id) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}