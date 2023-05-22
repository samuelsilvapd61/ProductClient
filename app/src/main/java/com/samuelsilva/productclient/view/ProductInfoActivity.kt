package com.samuelsilva.productclient.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.databinding.ActivityProductInfoBinding
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.viewmodel.ProductInfoViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ProductInfoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductInfoBinding
    private lateinit var viewModel: ProductInfoViewModel
    private lateinit var product: ProductModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProductInfoViewModel::class.java)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageGarbage.setOnClickListener(this)
        binding.imageBack.setOnClickListener(this)

        fillProductValues()

        observe()
    }

    private fun fillProductValues() {
        // Obter o Bundle enviado pela MainActivity
        val bundle = intent.getBundleExtra("bundleProduct")

        // Verificar se o Bundle não é nulo
        if (bundle != null) {
            // Obter o product do Bundle
            product = bundle.getParcelable<ProductModel>("product")!!

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
            val bundle = Bundle()
            bundle.putParcelable("product", product)

            // Criar a Intent para abrir a próxima activity
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("bundleProduct", bundle)

            // Iniciar a próxima activity
            startActivity(intent)

        } else if (v.id == binding.imageGarbage.id) {
            // Apaga o produto
            handleDelete()

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handleDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.remove_product_title)
            .setMessage(R.string.remove_product_message)
            .setPositiveButton(R.string.yes) { dialog, which ->
                viewModel.delete(product.id!!)
            }
            .setNeutralButton(R.string.cancel, null)
            .show()
    }


    private fun observe() {
        viewModel.status.observe(this) {
            if (it.status()) {
                val message = getString(R.string.product_removed_successfully)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("snackbar_message", message)
                startActivity(intent)
                finish()
            } else {
                if (it.expiredLogin()) {
                    val snackbar =
                        Snackbar.make(binding.root, it.message(), Snackbar.LENGTH_INDEFINITE)
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