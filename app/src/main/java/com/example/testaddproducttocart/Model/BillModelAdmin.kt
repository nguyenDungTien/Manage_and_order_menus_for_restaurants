package com.example.testaddproducttocart.Model

class BillModelAdmin() {
    var isChecked: Boolean?=null
    var time: String = ""
    var orderer: String = ""
    var place_of_delivery: String = ""
    var order_details: String = ""
    var totalPrice: String = ""

    constructor(
        time: String,
        customer: String,
        address: String,
        itemList: String,
        totalPrice: String
    ) : this() {
        this.time = time
        this.orderer = customer
        this.place_of_delivery = address
        this.order_details = itemList
        this.totalPrice = totalPrice
    }
}