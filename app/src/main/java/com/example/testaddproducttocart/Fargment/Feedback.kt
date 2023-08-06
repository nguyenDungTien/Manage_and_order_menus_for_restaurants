package com.example.testaddproducttocart.Fargment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testaddproducttocart.Model.FeedbackUser
import com.example.testaddproducttocart.R
import com.google.firebase.database.FirebaseDatabase

class Feedback: Fragment() {
    private lateinit var mView:View
    private lateinit var userPhone: EditText
    private lateinit var userName: EditText
    private lateinit var userEmail: EditText
    private lateinit var userFeedback: EditText
    private lateinit var btnSendFeedback: Button
    private lateinit var database: FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView =  inflater.inflate(R.layout.feedback_fragment,container,false)
        initUi()
        initListener()
        return mView
    }
    private fun initUi() {
        userFeedback=mView.findViewById(R.id.edt_feedback_user)
        userName=mView.findViewById(R.id.edt_name_user)
        userPhone=mView.findViewById(R.id.edt_phone_user)
        userEmail=mView.findViewById(R.id.edt_email_user)
        btnSendFeedback=mView.findViewById(R.id.btn_add_feedback)
    }
    private fun initListener() {
        btnSendFeedback.setOnClickListener {
            val nameUser=userName.text.toString()
            val phoneUser=userPhone.text.toString()
            val emailUser=userEmail.text.toString()
            val feedbackUser=userFeedback.text.toString()
            database = FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/")
            val reference = database.getReference("User Feedback")
            val feedback= FeedbackUser(nameUser, phoneUser, emailUser, feedbackUser)
            reference.child(phoneUser).setValue(feedback).addOnSuccessListener {
                Toast.makeText(context,"Saved", Toast.LENGTH_SHORT).show()
                userName.text.clear()
                userFeedback.text.clear()
                userPhone.text.clear()
                userEmail.text.clear()
            }.addOnFailureListener{
                Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }
}