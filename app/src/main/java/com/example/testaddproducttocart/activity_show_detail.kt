package com.example.testaddproducttocart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class activity_show_detail : AppCompatActivity() {
    private lateinit var nameItem:TextView
    private lateinit var imgItem:ImageView
    private lateinit var descItem:TextView
    private lateinit var discountedPrice :TextView
    private lateinit var btnBack:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)
        unitUi()

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            descItem.text = bundle.getString("Description")
            nameItem.text = bundle.getString("Name")
            val price = bundle.getString("Price")?.toFloatOrNull() ?: 0.0f
            val promotionPrice = bundle.getString("PromotionPrice")?.toFloatOrNull() ?: 0.0f
            val discountedPrice1 = price - price * promotionPrice / 100.0


            val formattedDiscountedPrice = String.format("%.2f", discountedPrice1)

            discountedPrice.text = formattedDiscountedPrice
            Glide.with(this).load(bundle.getString("Image")).override(200,200).into(imgItem)
        }

        initListener()
    }



    private fun unitUi() {
        nameItem=findViewById(R.id.detailTitle)
        imgItem=findViewById(R.id.detailImage)
        descItem=findViewById(R.id.detailDesc)
        discountedPrice =findViewById(R.id.detailPrice)
        btnBack=findViewById(R.id.btnBack)

    }
    private fun initListener() {
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }
}