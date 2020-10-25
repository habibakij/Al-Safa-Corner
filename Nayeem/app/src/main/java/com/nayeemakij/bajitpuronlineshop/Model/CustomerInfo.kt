package com.nayeemakij.bajitpuronlineshop.Model

data class CustomerInfo( val customerName:String, val customerPhone: String, val customerAddress: String, val productName:String,
                        val productPrize: String, val productQuantity: String){
    constructor(): this ("", "", "", "","","")
}