package com.example.testaddproducttocart.Listener

import com.example.testaddproducttocart.Model.CartModel


interface ICartLoadListener {
    fun onLoadCartSuccess(cartModelList:List<CartModel>)
    fun onLoadCartFailed(message:String?)
}