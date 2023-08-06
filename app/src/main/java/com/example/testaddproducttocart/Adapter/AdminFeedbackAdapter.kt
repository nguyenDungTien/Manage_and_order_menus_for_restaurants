package com.example.testaddproducttocart.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testaddproducttocart.Model.FeedbackUser
import com.example.testaddproducttocart.R

class AdminFeedbackAdapter(val context: Context, private val listData:List<FeedbackUser>):
    RecyclerView.Adapter<AdminFeedbackAdapter.AdminFeedbackViewHolder>() {
    class AdminFeedbackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameUser: TextView
        val feedbackUser: TextView
        private val btnDeleteFeedback: ImageView
        val phoneUser: TextView
        val emailUser: TextView
        init {
            nameUser=itemView.findViewById(R.id.tv_name_user)
            feedbackUser=itemView.findViewById(R.id.tv_feedback_user)
            btnDeleteFeedback=itemView.findViewById(R.id.img_delete)
            phoneUser=itemView.findViewById(R.id.tv_phone_user)
            emailUser=itemView.findViewById(R.id.tv_email_user)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminFeedbackViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_feedback_user,parent,false)
        return AdminFeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminFeedbackViewHolder, position: Int) {
        holder.nameUser.text=listData[position].nameUser
        holder.feedbackUser.text=listData[position].feedbackUser
        holder.phoneUser.text=listData[position].phoneUser
        holder.emailUser.text=listData[position].emailUser
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}
