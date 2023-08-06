package com.example.testaddproducttocart.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testaddproducttocart.Model.InputModel
import com.example.testaddproducttocart.R

class AdminItemAdapter(val context: Context, private var dataList: List<InputModel>) :
    RecyclerView.Adapter<AdminItemAdapter.AdminItemViewHolder>() {

    class AdminItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameItem: TextView? = null
        var descItem: TextView? = null
        var priceItem: TextView? = null
        var imageItem: ImageView? = null
        private var editItem: ImageView? = null
        private var deleteItem: ImageView? = null
        var pricePromotionItem: TextView? = null

        init {
            nameItem = itemView.findViewById(R.id.tv_name_item)
            descItem = itemView.findViewById(R.id.tv_desc_item)
            priceItem = itemView.findViewById(R.id.tv_price)
            priceItem?.apply {
                this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            imageItem = itemView.findViewById(R.id.img_item)
            editItem = itemView.findViewById(R.id.img_edit)
            deleteItem = itemView.findViewById(R.id.img_delete)
            pricePromotionItem = itemView.findViewById(R.id.tv_price_promotion)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_admin, parent, false)
        return AdminItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminItemViewHolder, position: Int) {
        Glide.with(context)
            .load(dataList[position].imgItem)
            .into(holder.imageItem!!)
        holder.nameItem!!.text = StringBuffer().append(dataList[position].nameItem)
        holder.descItem!!.text = dataList[position].descItem
        holder.priceItem!!.text = dataList[position].priceItem
        holder.pricePromotionItem!!.text =
            (dataList[position].priceItem.toDouble() - dataList[position].priceItem.toDouble() * dataList[position].promotionItem.toDouble() / 100).toString()

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<InputModel>) {
        dataList=searchList
        notifyDataSetChanged()
    }


}


