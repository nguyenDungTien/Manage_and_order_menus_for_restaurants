package com.example.testaddproducttocart.Model

import java.io.Serializable
import java.lang.StringBuilder

data class BillModel(
    var time: String? = null,
    var customer: String? = null,
    var address: String? = null,
    var itemList: List<CartModel>
) : Serializable {

    fun getTotalOrder(): String {
        if (itemList.isEmpty()) {
            return "0 VND"
        }
        var sum = 0.0
        for (item in itemList) {
            sum += item.totalPrice
        }
        return sum.toString()
    }

    fun getListProduct(): String {
        if (itemList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for (item in itemList) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("\n")
            }
            stringBuilder.append(item.name + " Số lượng: " + item.quantity)
        }
        return stringBuilder.toString()
    }
}





