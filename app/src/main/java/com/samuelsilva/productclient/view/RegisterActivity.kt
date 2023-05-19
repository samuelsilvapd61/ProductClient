package com.samuelsilva.productclient.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.databinding.ActivityRegisterBinding
import com.samuelsilva.productclient.service.model.ProductRequest
import com.samuelsilva.productclient.viewmodel.RegisterViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var quantity: EditText
    private lateinit var product: ProductRequest

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quantity = binding.editQuantity
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Esconder a barra superior que não serve pra nada
        supportActionBar?.hide()

        // Eventos de click
        binding.fab.setOnClickListener(this)
        binding.imageQrCode.setOnClickListener(this)
        binding.imageMinus.setOnClickListener(this)
        binding.imagePlus.setOnClickListener(this)
        binding.imageBack.setOnClickListener(this)

        // Observadores
        observe()

    }

    override fun onClick(v: View) {
        if (v.id == binding.fab.id) {
            // Salva o produto
            handleSave()

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

    private fun handleSave() {
        // Instancia o produto a ser adicionado no servidor
        val name = binding.editName.text.toString().takeIf { it.isNotBlank() }
        val description = binding.editDescription.text.toString().takeIf { it.isNotBlank() }
        val category = binding.editCategory.text.toString().takeIf { it.isNotBlank() }
        val productBrand = binding.editBrand.text.toString().takeIf { it.isNotBlank() }
        val provider = binding.editProvider.text.toString().takeIf { it.isNotBlank() }
        val quantity = binding.editQuantity.text.toString().toLongOrNull()
        val barCode = binding.editBarCode.text.toString().takeIf { it.isNotBlank() }
        val fabricationDate = formatDate(binding.editFabricationDate.text.toString())
        val expirationDate = formatDate(binding.editExpirationDate.text.toString())

        val product = ProductRequest().apply {
            this.name = name
            this.description = description
            this.category = category
            this.productBrand = productBrand
            this.provider = provider
            this.quantity = quantity
            this.barCode = barCode
            this.fabricationDate = fabricationDate
            this.expirationDate = expirationDate
        }

        viewModel.saveProduct(product, false)
    }

    private fun formatDate(dateString: String): String? {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } catch (e: ParseException) {
            dateString
        }
    }

    private fun observe() {
        viewModel.status.observe(this) {
            if (it.status()) {
                Snackbar.make(binding.root, "Produto cadastrado com sucesso.", Snackbar.LENGTH_SHORT).show()
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
                RegisterActivity.CAMERA_PERMISSION_REQUEST_CODE
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
                Snackbar.make(binding.root, getString(R.string.camera_unavailable), Snackbar.LENGTH_LONG).show()
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
                    setFilterValuesToEditTexts(product)
                } catch (e: Exception) {
                    Snackbar.make(binding.root, getString(R.string.incompatible_qr_code), Snackbar.LENGTH_LONG).show()
                }

            } else {
                // Nenhum QRCode foi lido
                Snackbar.make(binding.root, getString(R.string.qr_code_was_not_read), Snackbar.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun convertQrcodeToProduct(response: String): ProductRequest {
        return Gson().fromJson(response, ProductRequest::class.java)
    }

    private fun setFilterValuesToEditTexts(product: ProductRequest) {

        binding.editName.setText(product.name ?: "")
        binding.editDescription.setText(product.description ?: "")
        binding.editCategory.setText(product.category ?: "")
        binding.editBrand.setText(product.productBrand ?: "")
        binding.editProvider.setText(product.provider ?: "")
        binding.editQuantity.setText(product.quantity?.toString() ?: "")
        binding.editBarCode.setText(product.barCode ?: "")

        product.fabricationDate?.let {
            if (correctFormatDate(it))
                binding.editFabricationDate.setText(it)
        }

        product.expirationDate?.let {
            if (correctFormatDate(it))
                binding.editExpirationDate.setText(it)
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