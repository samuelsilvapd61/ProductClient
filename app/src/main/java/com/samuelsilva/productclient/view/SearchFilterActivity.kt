package com.samuelsilva.productclient.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.databinding.ActivitySearchFilterBinding
import com.samuelsilva.productclient.service.model.ProductModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SearchFilterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySearchFilterBinding
    private lateinit var quantity: EditText
    private var product: ProductModel? = null

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
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
            handleCamera()

        } else if (v.id == binding.imageMinus.id) {
            handleMinus()

        } else if (v.id == binding.imagePlus.id) {
            handlePlus()

        } else if (v.id == binding.imageBack.id) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handleCamera() {
        // Verifica se a permissão da câmera foi concedida
        // Se não foi concedida, aplicativo pede a permissão
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicita permissão da câmera
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Verifica se a câmera está disponível no dispositivo
            if (isCameraAvailable()) {
                val integrator = IntentIntegrator(this)
                integrator.setBeepEnabled(false) // Desativa o som de beep
                integrator.setOrientationLocked(false) // Permite rotação da tela
                integrator.setPrompt(getString(R.string.point_the_camera)) // Mensagem exibida ao usuário
                integrator.initiateScan()
            } else {
                // Câmera indisponível
                Snackbar.make(
                    binding.root,
                    getString(R.string.camera_unavailable),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun isCameraAvailable(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /**
     * Pede autorização para uso da câmera.
     * Lê o QRCode ou exibe mensagem de erro.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            val result: IntentResult? =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null && result.contents != null) {
                val scannedData = result.contents // Dados do QRCode lidos

                try {
                    product = convertQrcodeToProduct(scannedData.toString())
                    setFilterValuesToEditTexts()
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.incompatible_qr_code),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

            } else {
                // Nenhum QRCode foi lido
                Snackbar.make(
                    binding.root,
                    getString(R.string.qr_code_was_not_read),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun convertQrcodeToProduct(response: String): ProductModel {
        return Gson().fromJson(response, ProductModel::class.java)
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
        val productFilter = product ?: ProductModel.productFilter

        binding.editId.setText(productFilter.id?.toString() ?: "")
        binding.editName.setText(productFilter.name ?: "")
        binding.editDescription.setText(productFilter.description ?: "")
        binding.editCategory.setText(productFilter.category ?: "")
        binding.editBrand.setText(productFilter.productBrand ?: "")
        binding.editProvider.setText(productFilter.provider ?: "")
        binding.editQuantity.setText(productFilter.quantity?.toString() ?: "")
        binding.editBarCode.setText(productFilter.barCode ?: "")
        binding.editFabricationDate.setText(correctFormatDate(productFilter.fabricationDate) ?: "")
        binding.editExpirationDate.setText(correctFormatDate(productFilter.expirationDate) ?: "")
        binding.editInclusionDate.setText(correctFormatDate(productFilter.inclusionDate) ?: "")

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
}