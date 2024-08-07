package com.arekok.ink.apteka.medicineNew

class MedicineData {
    var nameM:String? = null
    var price:String? = null
    var type:String? = null
    var img :String? = null
    var srok:String? = null
    var structure:String? = null
    var how :String? = null
    constructor(){}
    constructor(
        nameM:String?,
        price:String,
        type:String,
        img :String,
        srok:String,
        structure: String,
        how :String
    ){
        this.nameM = nameM
        this.price = price
        this.type = type
        this.img = img
        this.srok = srok
        this.structure = structure
        this.how = how
    }
}