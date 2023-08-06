package com.example.testaddproducttocart.Fargment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testaddproducttocart.Adapter.OrderAdapter
import com.example.testaddproducttocart.Model.BillModelAdmin
import com.example.testaddproducttocart.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminOrderFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<BillModelAdmin>
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.admin_order_fragment, container, false)
        initUi()

        recyclerView.layoutManager = LinearLayoutManager(context)
        orderList = ArrayList()
        db.collection("bill").get()
            .addOnSuccessListener { querySnapshot ->
                val billList = ArrayList<BillModelAdmin>()
                for (document in querySnapshot) {
                    val billModelAdmin = document.toObject(BillModelAdmin::class.java)
                    billList.add(billModelAdmin)
                }
                recyclerView.adapter = OrderAdapter(requireContext(), billList)

            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
            }

        return mView
    }

    private fun initUi() {
        recyclerView = mView.findViewById(R.id.recycler_view_list_order)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerView.adapter?.notifyDataSetChanged()
    }
}