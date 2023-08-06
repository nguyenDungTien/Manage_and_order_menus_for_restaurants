package com.example.testaddproducttocart.Model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class CartModel (
    var name:String?=null,
    var image:String?=null,
    var price:String?=null,
    var quantity:Int=0,
    var totalPrice:Float=0f
):Serializable  {

}