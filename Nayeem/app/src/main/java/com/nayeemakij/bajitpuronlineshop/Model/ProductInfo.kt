package com.nayeemakij.bajitpuronlineshop.Model

data class ProductInfo(val image:String, val id:String, val name:String, val prize: String, val size:String, val description:String){
    constructor(): this ("", "", "", "","","")
}