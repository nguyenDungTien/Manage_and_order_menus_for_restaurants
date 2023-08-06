package com.example.testaddproducttocart.Fargment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buildcart.evenbus.UpdateCartEvent
import com.example.testaddproducttocart.Adapter.MyCartAdapter
import com.example.testaddproducttocart.Listener.ICartLoadListener
import com.example.testaddproducttocart.MainActivity
import com.example.testaddproducttocart.Model.BillModel
import com.example.testaddproducttocart.Model.CartModel
import com.example.testaddproducttocart.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class Cart: Fragment(),ICartLoadListener {
    private lateinit var mView: View
    private lateinit var recycler_cart: RecyclerView
    private lateinit var txtTotal:TextView
    private lateinit var cartLoadListener: ICartLoadListener
    private lateinit var btnConfirm: Button

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent) {
        loadCartFromFirebase()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView= inflater.inflate(R.layout.cart_fragment,container,false)

        initUi()
        init()
        loadCartFromFirebase()
        initListener()

        return mView

    }

    private fun initListener() {
        btnConfirm.setOnClickListener {
            clickOpenBottomSheetDialogFragment()
        }
    }

    private fun clickOpenBottomSheetDialogFragment() {
        val listProduct = ArrayList<CartModel>()
        FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Cart").child("UNIQUE_NAME")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.name = cartSnapshot.key
                        listProduct.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(listProduct)

                    val bill = BillModel("","" ,"", listProduct)
                    val sheetDialogFragment = MyBottomSheetDialogFragment.newInstance(bill)

                    // Use childFragmentManager instead of supportFragmentManager
                    sheetDialogFragment.show(childFragmentManager, sheetDialogFragment.tag)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }
            })
    }


    private fun loadCartFromFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Cart").child("UNIQUE_NAME")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.name = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init() {
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(requireContext())
        recycler_cart.layoutManager = layoutManager
        recycler_cart.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
    }

    private fun initUi() {
        recycler_cart=mView.findViewById(R.id.recycler_cart)
        txtTotal=mView.findViewById(R.id.txtTotal)
        btnConfirm=mView.findViewById(R.id.btn_confirm)
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var sum =0.0
        var cartSum=0
        for (cartModel in cartModelList){
            sum+=cartModel.totalPrice
            cartSum+=cartModel.quantity

        }
        txtTotal.text= StringBuilder(sum.toString()).append(" VND")
        val adapter= MyCartAdapter(requireContext(), cartModelList)
        recycler_cart.adapter=adapter
        (activity as? MainActivity)?.updateBadge(cartSum)


    }

    override fun onLoadCartFailed(message: String?) {
        Toast.makeText(context,"onLoadCartFailed", Toast.LENGTH_SHORT).show()
    }
}