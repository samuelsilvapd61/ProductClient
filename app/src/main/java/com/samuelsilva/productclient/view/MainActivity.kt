package com.samuelsilva.productclient.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.samuelsilva.productclient.R
import com.samuelsilva.productclient.databinding.ActivityMainBinding
import com.samuelsilva.productclient.service.listener.ProductListener
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.view.adapter.ProductAdapter
import com.samuelsilva.productclient.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var nextPage: Boolean = false

    private val adapter = ProductAdapter()

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
        binding.imageClearFilter.setOnClickListener(this)
        binding.imageLogout.setOnClickListener(this)

        // Configura o adaptador que faz a listagem funcionar
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val layoutParams = binding.recyclerView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomToTop = binding.fab.id
        binding.recyclerView.layoutParams = layoutParams

        createProductOnClickListeners(this)

        handleScrollAction()

        showSnackbar()

        // Observadores
        observe()

        if (ProductModel.CREATOR.isAllFieldsEmpty()) {
            // Se nao tiver filtro, deixa o icone cinza
            binding.imageClearFilter.alpha = 0.3F
        } else {
            // Se tiver, deixa o icone branco
            binding.imageClearFilter.alpha = 1F
        }
    }

    private fun showSnackbar() {
        val snackbarMessage = intent.getStringExtra("snackbar_message")
        if (!snackbarMessage.isNullOrEmpty()) {
            val rootView = findViewById<View>(android.R.id.content)
            Snackbar.make(rootView, snackbarMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Verifica se o usuário já está no fim da lista e insiste em arrastar a tela,
     *  na intenção de carregar mais produtos
     */
    private fun handleScrollAction() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val itemCount = adapter.itemCount

                val lastVisibleChild = layoutManager.getChildAt(layoutManager.childCount - 1)
                val lastVisibleItemBottom = lastVisibleChild?.bottom ?: 0

                val isLastItemCompletelyVisible = lastVisibleItemBottom <= recyclerView.height

                if (lastVisibleItemPosition == itemCount - 1 && dy > 0 && isLastItemCompletelyVisible) {
                    // Exibe o ícone de carregamento abaixo da recyclerview
                    binding.progressBar.visibility = View.VISIBLE

                    // Modifica o layout para o ícone de carregamento aparecer na tela
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.root)
                    constraintSet.clear(recyclerView.id, ConstraintSet.BOTTOM)
                    constraintSet.connect(
                        recyclerView.id,
                        ConstraintSet.BOTTOM,
                        binding.progressBar.id,
                        ConstraintSet.TOP
                    )
                    constraintSet.applyTo(binding.root)

                    // Chama a função para buscar mais elementos
                    loadMoreItems()
                }
            }
        })
    }

    /**
     * Busca mais produtos no servidor e incrementa a lista já existente na tela
     */
    private fun loadMoreItems() {
        viewModel.list(ProductModel.productFilter, true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.list(ProductModel.productFilter, false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.fab.id -> {
                startActivity(Intent(this, RegisterActivity::class.java))

            }
            binding.imageFilter.id -> {
                startActivity(Intent(this, SearchFilterActivity::class.java))
                finish()

            }
            binding.imageClearFilter.id -> {
                ProductModel.productFilter = ProductModel().apply {
                    id = null
                    name = null
                    description = null
                    category = null
                    productBrand = null
                    provider = null
                    quantity = null
                    barCode = null
                    fabricationDate = null
                    expirationDate = null
                    inclusionDate = null
                }
                viewModel.list(ProductModel.productFilter, false)
                binding.imageClearFilter.alpha = 0.3F

            }
            binding.imageLogout.id -> {
                handleLogout()
            }
        }
    }

    private fun observe() {
        viewModel.status.observe(this) {
            if (it.status()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
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
        viewModel.nextPage.observe(this) {
            nextPage = it
        }
        viewModel.products.observe(this) {
            adapter.updateTasks(it, nextPage)
            resetLayout()
        }
    }

    /**
     * Desfaz as alterações de layout após carregar a próxima página da consulta
     */
    private fun resetLayout() {
        // Ajusta o layout para que o RecyclerView fique acima do FAB
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.clear(binding.recyclerView.id, ConstraintSet.BOTTOM)
        constraintSet.connect(
            binding.recyclerView.id,
            ConstraintSet.BOTTOM,
            binding.fab.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(binding.root)

        // Oculta o ícone de carregamento
        binding.progressBar.visibility = View.GONE
    }

    private fun handleLogout() {
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * Cria um listener individual para cada produto sendo exibido na tela
     */
    private fun createProductOnClickListeners(context: MainActivity) {
        val listener = object : ProductListener {
            override fun onListClick(product: ProductModel) {
                val bundle = Bundle()
                bundle.putParcelable("product", product)

                // Criar a Intent para abrir a próxima activity
                val intent = Intent(context, ProductInfoActivity::class.java)
                intent.putExtra("bundleProduct", bundle)

                // Iniciar a próxima activity
                startActivity(intent)
            }

        }
        adapter.attachListener(listener)
    }
}