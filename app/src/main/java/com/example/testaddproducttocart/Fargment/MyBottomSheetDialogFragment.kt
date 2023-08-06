package com.example.testaddproducttocart.Fargment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.testaddproducttocart.Model.BillModel
import com.example.testaddproducttocart.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class MyBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var mOrder:BillModel
    private lateinit var tv_total_price:TextView
    private lateinit var tv_order_details:TextView
    private lateinit var edt_Orderer:EditText
    private lateinit var edt_place_of_delivery:EditText
    private lateinit var btn_cancel:Button
    private lateinit var btn_order:Button
    private var db=Firebase.firestore

    companion object {
        private const val KEY_ORDER = "order"


        // Factory method to create a new instance of MyBottomSheetDialogFragment
        fun newInstance(order: BillModel): MyBottomSheetDialogFragment {
            val myBottomSheetDialogFragment = MyBottomSheetDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_ORDER, order)
            myBottomSheetDialogFragment.arguments = bundle
            return myBottomSheetDialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundleReceive:Bundle?= arguments
        if (bundleReceive!=null){
            mOrder= bundleReceive.get(KEY_ORDER) as BillModel
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog:BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val viewDialog:View = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet,null)
        bottomSheetDialog.setContentView(viewDialog)
        initView(viewDialog)
        setDataOrder()
        initListener()
        return bottomSheetDialog
    }

    private fun initListener() {
        btn_order.setOnClickListener {
            val isChecked: Boolean = false
            val orderer = edt_Orderer.text.toString().trim()
            val orderDetails = mOrder.getListProduct()
            val totalPrice = mOrder.getTotalOrder()
            val placeOfDelivery = edt_place_of_delivery.text.toString().trim()
            val currentDate= DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
            val billMap = hashMapOf(
                "total_price" to totalPrice,
                "order_details" to orderDetails,
                "orderer" to orderer,
                "place_of_delivery" to placeOfDelivery,
                "time" to currentDate,
                "isChecked" to isChecked
            )
            val billId = FirebaseAuth.getInstance().currentUser!!.uid
            db.collection("bill").document(currentDate).set(billMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Mua hàng thành công", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Mua hàng không thành công", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun initView(viewDialog: View) {
        tv_total_price = viewDialog.findViewById(R.id.tv_total_price)
        tv_order_details = viewDialog.findViewById(R.id.tv_order_details)
        edt_Orderer = viewDialog.findViewById(R.id.edt_Orderer)
        edt_place_of_delivery = viewDialog.findViewById(R.id.edt_place_of_delivery)
        btn_cancel = viewDialog.findViewById(R.id.btn_cancel)
        btn_order = viewDialog.findViewById(R.id.btn_order)

    }
    private fun setDataOrder() {
        tv_total_price.text= StringBuilder(mOrder.getTotalOrder()).append(" VND")
        tv_order_details.text=mOrder.getListProduct()



    }
}