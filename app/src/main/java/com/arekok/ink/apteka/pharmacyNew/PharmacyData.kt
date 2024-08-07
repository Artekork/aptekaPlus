package com.arekok.ink.apteka.pharmacyNew

class PharmacyData {
    var nameP:String? = null
    var work:String? = null
    var loc1:Double? = null
    var loc2:Double? = null
    var location:String? = null
    var end:String? = null
    var start:String? = null
    constructor(){}

    constructor(
        nameP:String?,
        work:String,
        loc1:Double,
        loc2:Double,
        location:String,
        end:String,
        start:String
    ){
        this.nameP = nameP
        this.work = work
        this.loc1 = loc1
        this.loc2 = loc2
        this.location = location
        this.end = end
        this.start = start
    }
}