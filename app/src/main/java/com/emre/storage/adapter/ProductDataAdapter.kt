package com.emre.storage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emre.storage.databinding.RecyclerRowBinding
import com.emre.storage.model.ProductData

class ProductDataAdapter(private val dataList: ArrayList<ProductData>): RecyclerView.Adapter<ProductDataAdapter.DataHolder>() {

    class DataHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        val productName = dataList[position].productName
        val stock = dataList[position].stock
        val price = dataList[position].price.toDouble() * stock.toDouble()
        val formattedPrice = String.format("%.2f", price)
        val color = dataList[position].color
        val currency = dataList[position].currency

        holder.binding.rcColorText.text = color
        holder.binding.rcStockText.text = stock
        holder.binding.rcPriceText.text = "$formattedPrice $currency"

    }
}