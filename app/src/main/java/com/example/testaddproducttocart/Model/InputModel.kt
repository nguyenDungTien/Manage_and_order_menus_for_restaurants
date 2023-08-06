package com.example.testaddproducttocart.Model

class InputModel(
    val nameItem:String,
    val imgItem:String?,
    val typeItem:String,
    val descItem:String,
    val priceItem:String,
    val promotionItem:String) {
    constructor() : this("", "", "", "", "", "")
}