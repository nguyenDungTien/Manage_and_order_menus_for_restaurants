package com.example.testaddproducttocart.Fargment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testaddproducttocart.Adapter.AdminItemAdapter
import com.example.testaddproducttocart.Model.InputModel
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import com.example.testaddproducttocart.R

class AdminHomeFragment:Fragment() {
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var dataList:MutableList<InputModel>
    private lateinit var mDatabaseReference:DatabaseReference
    private lateinit var eventListener:ValueEventListener
    private lateinit var mView:View
    private lateinit var searchView: SearchView
    private lateinit var adapter: AdminItemAdapter
    private lateinit var dialog:AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView= inflater.inflate(R.layout.admin_home_fragment,container,false)
        intiUi()
        val gridLayoutManager = GridLayoutManager(context, 1)
        mRecyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        dialog = builder.create()
        dialog.show()

        loadDrinkFromFirebase()
        initListener()






    return mView
    }

    private fun initListener() {
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun loadDrinkFromFirebase() {
        dataList = ArrayList()
        adapter = AdminItemAdapter(requireContext(), dataList)
        mRecyclerView.adapter = adapter
        mDatabaseReference = FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Items")

        dialog.show()
        eventListener = mDatabaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(InputModel::class.java)
                    if (dataClass != null) {
                        dataList.add(dataClass)
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

    private fun intiUi() {
        mRecyclerView=mView.findViewById(R.id.rv_item)
        searchView=mView.findViewById(R.id.search)

    }
    private fun searchList(newText: String) {
        val searchList = ArrayList<InputModel>()
        for (dataClass in dataList) {
            if (dataClass.nameItem.lowercase().contains(newText.lowercase())){
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Retrieve data and update the dataList
//        fetchData()
//    }

//    override fun onStart() {
//        super.onStart()
//        fetchData()
//    }
    override fun onResume() {
        super.onResume()
        loadDrinkFromFirebase()
    }
//    private fun fetchData() {
//        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
//        mRecyclerView.layoutManager= gridLayoutManager
//        dataList = ArrayList()
//        adapter=AdminItemAdapter(requireContext(), dataList )
//        mRecyclerView.adapter=adapter
//        mDatabaseReference=FirebaseDatabase.getInstance("https://project-3-40490-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Items")
//        eventListener = mDatabaseReference!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                dataList.clear()
//                for (itemSnapshot in snapshot.children) {
//                    val dataClass = itemSnapshot.getValue(DataImportModel::class.java)
//                    if (dataClass != null) {
//                        dataList.add(dataClass)
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }

}