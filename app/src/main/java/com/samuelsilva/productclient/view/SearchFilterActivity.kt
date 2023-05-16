package com.samuelsilva.productclient.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.samuelsilva.productclient.databinding.ActivitySearchFilterBinding
import com.samuelsilva.productclient.service.model.ProductModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchFilterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySearchFilterBinding
    private lateinit var quantity: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quantity = binding.editQuantity

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageQrCode.setOnClickListener(this)
        binding.imageMinus.setOnClickListener(this)
        binding.imagePlus.setOnClickListener(this)
        binding.imageBack.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setFilterValuesToEditTexts()
    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            handleFilter()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()

        } else if (v.id == binding.imageQrCode.id) {
            // Abre a camera para ler o QRCode

        } else if (v.id == binding.imageMinus.id) {
            handleMinus()

        } else if (v.id == binding.imagePlus.id) {
            handlePlus()

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handlePlus() {
        val text = quantity.text.toString()
        if (text.isNotBlank()) {
            var valor = text.toInt()
            valor++
            quantity.setText(valor.toString())
        } else {
            quantity.setText("0")
        }
    }

    private fun handleMinus() {
        val text = quantity.text.toString()
        if (text.isNotBlank()) {
            var valor = text.toInt()
            if (valor > 0) {
                valor--
            }
            quantity.setText(valor.toString())
        } else {
            quantity.setText("0")
        }
    }

    /**
     * Configura o filtro com base nos campos
     */
    private fun handleFilter() {
        // Configura a filtragem da busca e volta para a tela principal
        val id = binding.editId.text.toString().toLongOrNull()
        val name = binding.editName.text.toString().takeIf { it.isNotBlank() }
        val description = binding.editDescription.text.toString().takeIf { it.isNotBlank() }
        val category = binding.editCategory.text.toString().takeIf { it.isNotBlank() }
        val productBrand = binding.editBrand.text.toString().takeIf { it.isNotBlank() }
        val provider = binding.editProvider.text.toString().takeIf { it.isNotBlank() }
        val quantity = binding.editQuantity.text.toString().toLongOrNull()
        val barCode = binding.editBarCode.text.toString().takeIf { it.isNotBlank() }
        val fabricationDate = binding.editFabricationDate.text.toString().takeIf { it.isNotBlank() }
        val expirationDate = binding.editExpirationDate.text.toString().takeIf { it.isNotBlank() }
        val inclusionDate = binding.editInclusionDate.text.toString().takeIf { it.isNotBlank() }

        ProductModel.productFilter.id = id
        ProductModel.productFilter.name = name
        ProductModel.productFilter.description = description
        ProductModel.productFilter.category = category
        ProductModel.productFilter.productBrand = productBrand
        ProductModel.productFilter.provider = provider
        ProductModel.productFilter.quantity = quantity
        ProductModel.productFilter.barCode = barCode
        ProductModel.productFilter.fabricationDate = fabricationDate
        ProductModel.productFilter.expirationDate = expirationDate
        ProductModel.productFilter.inclusionDate = inclusionDate
    }

    private fun setFilterValuesToEditTexts() {
        val productFilter = ProductModel.productFilter

        binding.editId.setText(productFilter.id?.toString() ?: "")
        binding.editName.setText(productFilter.name ?: "")
        binding.editDescription.setText(productFilter.description ?: "")
        binding.editCategory.setText(productFilter.category ?: "")
        binding.editBrand.setText(productFilter.productBrand ?: "")
        binding.editProvider.setText(productFilter.provider ?: "")
        binding.editQuantity.setText(productFilter.quantity?.toString() ?: "")
        binding.editBarCode.setText(productFilter.barCode ?: "")

        productFilter.fabricationDate?.let {
            if (correctFormatDate(it))
                binding.editFabricationDate.setText(it)
        }

        productFilter.expirationDate?.let {
            if (correctFormatDate(it))
                binding.editExpirationDate.setText(it)
        }

        productFilter.inclusionDate?.let {
            if (correctFormatDate(it))
                binding.editInclusionDate.setText(it)
        }
    }

    private fun correctFormatDate(date: String): Boolean {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        inputFormat.isLenient = false

        return try {
            inputFormat.parse(date)
            true
        } catch (e: ParseException) {
            false
        }
    }
}