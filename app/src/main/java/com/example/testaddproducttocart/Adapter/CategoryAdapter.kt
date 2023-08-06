package com.example.testaddproducttocart.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import com.example.testaddproducttocart.Model.CategoryModel
import com.example.testaddproducttocart.R


class CategoryAdapter(val context: Context,val ds: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {
    lateinit var categoryName: TextView
    lateinit var categoryPic: ImageView

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.apply {
            categoryName = findViewById(R.id.categoryName)
            categoryPic = findViewById(R.id.categoryPic)
            categoryPic.setImageResource(ds[position].pic)
            categoryName.text = ds[position].title
        }
        when (position) {
            0 -> holder.itemView.setBackgroundColor(Color.parseColor("#fef4e5"))
            1 -> holder.itemView.setBackgroundColor(Color.parseColor("#E5F1FE"))
            2 -> holder.itemView.setBackgroundColor(Color.parseColor("#ffd1b3"))
            3 -> holder.itemView.setBackgroundColor(Color.parseColor("#b3ecff"))
            4 -> holder.itemView.setBackgroundColor(Color.parseColor("#F9E4E4"))
            5 -> holder.itemView.setBackgroundColor(Color.parseColor("#ccb3ff"))
            6 -> holder.itemView.setBackgroundColor(Color.parseColor("#ccffb3"))
            7 -> holder.itemView.setBackgroundColor(Color.parseColor("#ffb3ff"))
        }
        holder.itemView.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return ds.size
    }
}
