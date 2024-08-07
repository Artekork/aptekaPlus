package com.arekok.ink.apteka.basket

class BasketData {
    var nameM:String? = null
    var type:String? = null
    var price:String? = null
    constructor(){}
    constructor(
        nameM:String?,
        type:String?,
        price:String?
    ){
        this.nameM = nameM
        this.type = type
        this.price = price
    }
}