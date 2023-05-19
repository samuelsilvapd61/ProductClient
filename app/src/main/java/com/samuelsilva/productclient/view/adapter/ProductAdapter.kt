package com.samuelsilva.productclient.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samuelsilva.productclient.databinding.RowProductBinding
import com.samuelsilva.productclient.service.listener.ProductListener
import com.samuelsilva.productclient.service.model.ProductModel
import com.samuelsilva.productclient.view.viewholder.ProductViewHolder

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>() {

    private var listProducts: MutableList<ProductModel> = mutableListOf()
    private lateinit var listener: ProductListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(itemBinding, listener)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        println(position)
        holder.bindData(listProducts[position])
    }

    override fun getItemCount(): Int {
        return listProducts.count()
    }

    fun updateTasks(list: List<ProductModel>, nextPage: Boolean) {
        if (!nextPage) {
            listProducts.clear()
        }
        listProducts.addAll(list)
        notifyDataSetChanged()
    }

    fun attachListener(taskListener: ProductListener) {
        listener = taskListener
    }

}