package com.samuelsilva.productclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.samuelsilva.productclient.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()
//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = getString(R.string.app_name)
//        supportActionBar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
//        supportActionBar?.setDefaultTitleTextColor(ContextCompat.getColor(this, R.color.white))

        // Eventos de click
        binding.buttonLogin.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v.id == binding.buttonLogin.id) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}