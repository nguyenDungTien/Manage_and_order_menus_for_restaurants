package com.example.testaddproducttocart.Fargment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testaddproducttocart.Adapter.AdminFeedbackAdapter
import com.example.testaddproducttocart.Model.FeedbackUser
import com.google.firebase.database.*
import com.example.testaddproducttocart.R

class AdminFeedbackFragment:Fragment() {
    private lateinit var mView:View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var dataList:ArrayList<FeedbackUser>
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var eventListener: ValueEventListener
    private lateinit var adapter: AdminFeedbackAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView= inflater.inflate(R.layout.admin_feedback_fragment,container,false)
        unitUi()
        fetchData()

        return mView
    }

    private fun fetchData() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        mRecyclerView.layoutManager= gridLayoutManager
        dataList = ArrayList()
        adapter= AdminFeedbackAdapter(requireContext(), dataList )
        mRecyclerView.adapter=adapter
        mDatabaseReference= FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User Feedback")
        eventListener = mDatabaseReference!!.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(FeedbackUser::class.java)
                    if (dataClass != null) {
                        dataList.add(dataClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun unitUi() {
        mRecyclerView=mView.findViewById(R.id.rv_feedback_user)
    }
    override fun onResume() {
        super.onResume()
        fetchData()
    }
}

