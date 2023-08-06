package com.example.testaddproducttocart.Fargment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buildcart.evenbus.UpdateCartEvent
import com.example.testaddproducttocart.Adapter.CategoryAdapter
import com.example.testaddproducttocart.Adapter.ShowItemAdapter
import com.example.testaddproducttocart.Listener.ICartLoadListener
import com.example.testaddproducttocart.MainActivity
import com.example.testaddproducttocart.Model.CartModel
import com.example.testaddproducttocart.Model.CategoryModel
import com.example.testaddproducttocart.Model.InputModel
import com.example.testaddproducttocart.R
import com.example.testaddproducttocart.activity_upload
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class Home : Fragment(),ICartLoadListener {
    private lateinit var mView: View
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var adapter: ShowItemAdapter
    private lateinit var dialog:AlertDialog
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var itemList: MutableList<InputModel>
    private lateinit var cartLoadListener: ICartLoadListener
    private lateinit var homeFragmentLayout:RelativeLayout
    private lateinit var rvCategory: RecyclerView


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
        countCartFromFirebase()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.home_fragment, container, false)
        unitUi()
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager
        cartLoadListener=this

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        dialog = builder.create()
        dialog.show()
        recyclerViewCategory()
        loadDrinkFromFirebase()
        countCartFromFirebase()




        initListener()

        return mView
    }
    private fun countCartFromFirebase() {
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

    private fun loadDrinkFromFirebase() {
        itemList = ArrayList()
        adapter = ShowItemAdapter(requireContext(), itemList,cartLoadListener)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Items")
        dialog.show()
        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(InputModel::class.java)
                    if (dataClass != null) {
                        itemList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })
    }


    private fun unitUi() {
        fab = mView.findViewById(R.id.fab)
        recyclerView = mView.findViewById(R.id.recyclerView)
        searchView=mView.findViewById(R.id.search)
        homeFragmentLayout=mView.findViewById(R.id.homeFragmentLayout)
        rvCategory=mView.findViewById(R.id.rvCategory)
    }

    private fun initListener() {
        fab.setOnClickListener {
            val intent = Intent(context, activity_upload::class.java)
            startActivity(intent)
        }
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle text submitted (if needed)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

    }

    private fun searchList(newText: String) {
        val filteredList = ArrayList<InputModel>()
        for (item in itemList) {
            if (item.nameItem!!.lowercase().contains(newText.lowercase())){
                filteredList.add(item)
            }
        }
        adapter.searchDataList(filteredList)

    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum=0
        for (cartModel in cartModelList){
            cartSum+=cartModel.quantity!!.toInt()
        }
        (activity as? MainActivity)?.updateBadge(cartSum)

    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(homeFragmentLayout, message!!, Snackbar.LENGTH_LONG).show()
    }
    private fun recyclerViewCategory() {
        val ds = mutableListOf<CategoryModel>()
        ds.add(CategoryModel(R.drawable.category_biryani, "Cơm"))
        ds.add(CategoryModel(R.drawable.category_noodles, "Mì"))
        ds.add(CategoryModel(R.drawable.category_pizza, "Pizza"))
        ds.add(CategoryModel(R.drawable.category_cake, "Bánh ngot"))
        ds.add(CategoryModel(R.drawable.category_cocktail, "Đồ uống"))
        ds.add(CategoryModel(R.drawable.category_smoothie, "Freeze"))
        ds.add(CategoryModel(R.drawable.category_tea, "Trà"))
        ds.add(CategoryModel(R.drawable.category_skewer, "Đồ ăn nhanh"))
        val adapter = CategoryAdapter(requireContext(),ds)
        rvCategory.adapter = adapter
        rvCategory.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }
}