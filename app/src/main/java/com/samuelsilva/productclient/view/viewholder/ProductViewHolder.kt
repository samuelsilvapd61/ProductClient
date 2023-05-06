package com.samuelsilva.productclient.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.samuelsilva.productclient.databinding.RowProductBinding
import com.samuelsilva.productclient.service.listener.ProductListener
import com.samuelsilva.productclient.service.model.ProductModel

class ProductViewHolder(private val itemBinding: RowProductBinding, val listener: ProductListener) :
    RecyclerView.ViewHolder(itemBinding.root) {

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(product: ProductModel) {

        itemBinding.textNameValue.text = product.name
        itemBinding.textBrandValue.text = product.productBrand
        itemBinding.textCategoryValue.text = product.category
        itemBinding.textQuantityValue.text = product.quantity.toString()

        itemBinding.root.setOnClickListener {
            // ação a ser executada ao clicar no item
            listener.onListClick(product)
        }

    }
}