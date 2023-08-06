package com.example.testaddproducttocart.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buildcart.evenbus.UpdateCartEvent
import com.example.testaddproducttocart.Listener.ICartLoadListener
import com.example.testaddproducttocart.Listener.IRecyclerClickListener
import com.example.testaddproducttocart.Model.CartModel
import com.example.testaddproducttocart.Model.InputModel
import com.example.testaddproducttocart.R
import com.example.testaddproducttocart.activity_show_detail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

class ShowItemAdapter(private val context: Context, private var itemList: List<InputModel>,private val cartListener: ICartLoadListener) :
    RecyclerView.Adapter<ShowItemAdapter.ShowItemViewHolder>() {
    class ShowItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView? = null
        var txtName: TextView? = null
        var txtPrice: TextView? = null
        var txtPricePromotion: TextView? = null
        var txtDesc: TextView? = null
        var recCard:CardView?=null
        var btnDetailsInformation:Button?=null
        private var clickListener:IRecyclerClickListener?=null
        fun setClickListener(clickListener: IRecyclerClickListener) {
            this.clickListener = clickListener
        }

        init {
            imageView = itemView.findViewById(R.id.imageItem) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtDesc = itemView.findViewById(R.id.txtDesc) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtPrice?.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            txtPricePromotion = itemView.findViewById(R.id.txtPromotionalPrice) as TextView
            recCard=itemView.findViewById(R.id.recCard) as CardView
            btnDetailsInformation=itemView.findViewById(R.id.btnDetailsInformation) as Button
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowItemViewHolder {
        return ShowItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShowItemViewHolder, position: Int) {
        Glide.with(context)
            .load(itemList[position].imgItem)
            .into(holder.imageView!!)
        holder.txtName!!.text = StringBuffer().append(itemList[position].nameItem)
        holder.txtPrice!!.text = StringBuffer(itemList[position].priceItem!!).append(" VND")
        holder.txtPricePromotion!!.text = StringBuffer(
            (itemList[position].priceItem!!.toFloat() - (itemList[position].priceItem!!.toFloat() * itemList[position].promotionItem!!.toFloat() / 100)).toInt().toString()
        ).append(" VND")
        holder.txtDesc!!.text = StringBuffer().append(itemList[position].descItem)
        holder.btnDetailsInformation!!.setOnClickListener {
            val intent = Intent(context, activity_show_detail::class.java)
            intent.putExtra("Image", itemList[position].imgItem)
            intent.putExtra("Description", itemList[position].descItem)
            intent.putExtra("Name", itemList[position].nameItem)
            intent.putExtra("Price", itemList[position].priceItem)
            intent.putExtra("PromotionPrice", itemList[position].promotionItem)
            context.startActivity(intent)
        }

        holder.setClickListener(object : IRecyclerClickListener {
            override fun onItemClickListener(view: View?, position: Int) {
                addCart(itemList[position])
            }
        })
    }

    private fun addCart(inputModel: InputModel) {
        val userCart = FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Cart").child("UNIQUE_NAME")
        userCart.child(inputModel.nameItem!!).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {//If item already in cart, just update
                    val cartModel = snapshot.getValue(CartModel::class.java)
                    val updateData: MutableMap<String, Any> = HashMap()
                    cartModel!!.quantity = (cartModel.quantity+1)
                    updateData["quantity"] = cartModel.quantity
                    updateData["totalPrice"] =
                        (cartModel.quantity * cartModel.price!!.toFloat())

                    userCart.child(inputModel.nameItem)
                        .updateChildren(updateData)
                        .addOnSuccessListener {
                            EventBus.getDefault().postSticky(UpdateCartEvent())
                            cartListener.onLoadCartFailed("Success add to cart")
                        }
                        .addOnFailureListener { e -> cartListener.onLoadCartFailed(e.message) }

                } else {//if item not in cart, add new
                    val cartModel = CartModel()
                    cartModel.name = inputModel.nameItem
                    cartModel.image = inputModel.imgItem
                    cartModel.price = (inputModel.priceItem!!.toFloat()-inputModel.priceItem.toFloat()*inputModel.promotionItem!!.toFloat()/100).toString()
                    cartModel.quantity = 1
                    cartModel.totalPrice = cartModel.price!!.toFloat()
                    userCart.child(inputModel.nameItem)
                        .setValue(cartModel)
                        .addOnSuccessListener {
                            EventBus.getDefault().postSticky(UpdateCartEvent())
                            cartListener.onLoadCartFailed("Success add to cart")
                        }
                        .addOnFailureListener { e -> cartListener.onLoadCartFailed(e.message) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cartListener.onLoadCartFailed(error.message)
            }
        })

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun searchDataList(searchList: ArrayList<InputModel>) {
        itemList=searchList
        notifyDataSetChanged()
    }
}