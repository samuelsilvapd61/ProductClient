package com.samuelsilva.productclient.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samuelsilva.productclient.databinding.ActivityProductInfoBinding
import com.samuelsilva.productclient.service.model.ProductModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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

        fillProductValues()
    }

    private fun fillProductValues() {
        // Obter o Bundle enviado pela MainActivity
        val bundle = intent.getBundleExtra("bundleProduct")

        // Verificar se o Bundle não é nulo
        if (bundle != null) {
            // Obter o product do Bundle
            val product = bundle.getParcelable<ProductModel>("product")

            // Verificar se o product não é null e preencher a tela com as informações
            if (product != null) {
                binding.textIdValue.text = product.id.toString()
                binding.textNameValue.text = product.name
                binding.textDescriptionValue.text = product.description
                binding.textCategoryValue.text = product.category
                binding.textBrandValue.text = product.productBrand
                binding.textQuantityValue.text = product.quantity.toString()
                binding.textProviderValue.text = product.provider
                binding.textBarCodeValue.text = product.barCode
                binding.textFabricationDateValue.text = product.fabricationDate
                binding.textExpirationDateValue.text = product.expirationDate
                binding.textInclusionDateValue.text = product.inclusionDate

                product.fabricationDate.let {
                    binding.textFabricationDateValue.text = correctFormatDate(it)
                }
                product.expirationDate.let {
                    binding.textExpirationDateValue.text = correctFormatDate(it)
                }
                product.inclusionDate.let {
                    binding.textInclusionDateValue.text = correctFormatDateTime(it)
                }
            }
        }
    }
    private fun correctFormatDate(date: String?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        inputFormat.isLenient = false
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        if (date == null) return null
        return try {
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate)
        } catch (e: ParseException) {
            null
        }
    }
    private fun correctFormatDateTime(dateTime: String?): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormat.isLenient = false
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        if (dateTime == null) return null
        return try {
            val parsedDateTime = inputFormat.parse(dateTime)
            outputFormat.format(parsedDateTime)
        } catch (e: ParseException) {
            null
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            // Abre a tela de cadastro, mas no modo de edição
            startActivity(Intent(this, RegisterActivity::class.java))

        } else if (v.id == binding.imageGarbage.id) {
            // Apaga o produto

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}