package com.example.testaddproducttocart.Fargment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.testaddproducttocart.R

class Contact: Fragment() {
    private lateinit var mview: View
    private lateinit var cvFacebook: CardView
    private lateinit var cvZalo: CardView
    private lateinit var cvGmail: CardView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mview = inflater.inflate(R.layout.contact_fragment,container,false)

        initUi()
        initListener()

        return mview
    }

    private fun initUi() {
        cvFacebook=mview.findViewById(R.id.cv_facebook)
        cvZalo=mview.findViewById(R.id.cv_zalo)
        cvGmail=mview.findViewById(R.id.cv_gmail)
    }

    private fun initListener() {
        cvFacebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.facebook.com/dunglioneI/")
            startActivity(intent)
        }
        cvZalo.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogPhone = layoutInflater.inflate(R.layout.dialog_phone, null)
            builder.setView(dialogPhone)
            val dialog: AlertDialog = builder.create()
            val btnExit: Button =dialogPhone.findViewById(R.id.btnCancel)
            btnExit.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        cvGmail.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogPhone = layoutInflater.inflate(R.layout.dialog_email, null)
            builder.setView(dialogPhone)
            val dialog: AlertDialog = builder.create()
            val btnExit: Button =dialogPhone.findViewById(R.id.btnCancel)
            btnExit.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}