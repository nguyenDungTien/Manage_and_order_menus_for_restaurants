package com.example.testaddproducttocart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testaddproducttocart.Model.BillModel
import com.example.testaddproducttocart.Model.BillModelAdmin
import com.example.testaddproducttocart.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderAdapter(private val context: Context, private val orderList:ArrayList<BillModelAdmin>) :RecyclerView.Adapter<OrderAdapter.OrderViewHolder>(){

    class OrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val totalPrice:TextView=itemView.findViewById(R.id.tv_total_price)
        val orderDetails:TextView=itemView.findViewById(R.id.tv_order_details)
        val orderName:TextView=itemView.findViewById(R.id.tv_oder_name)
        val orderPlace:TextView=itemView.findViewById(R.id.tv_place_of_delivery)
        val timeOrder:TextView=itemView.findViewById(R.id.tv_time_order)
        val checkBox:CheckBox=itemView.findViewById(R.id.checkBox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_order,parent,false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.totalPrice.text=orderList[position].totalPrice
        holder.orderDetails.text=orderList[position].order_details
        holder.orderName.text=orderList[position].orderer
        holder.orderPlace.text=orderList[position].place_of_delivery
        holder.timeOrder.text=orderList[position].time




        // Set background color based on checkbox state
        if (orderList[position].isChecked==true) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white3))
        }
        if (orderList[position].isChecked==false) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white2))
        }

        // Handle checkbox state changes
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            orderList[position].isChecked = isChecked
            if (isChecked) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white3))
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white2))
            }

            // Update the Firestore document with the new isChecked value
            val db = Firebase.firestore
            val billRef = db.collection("bill").document(orderList[position].time)
            billRef.update("isChecked", isChecked)
                .addOnSuccessListener {
                    // Update successful
                    Toast.makeText(context,"Đã giao hàng",Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    // Update failed
                    Toast.makeText(context,"Đã lỗi",Toast.LENGTH_SHORT).show()
                }
        }


    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}