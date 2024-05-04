package com.example.cryptocurrency

data class Item(
    val imageIcon: Int,
    val coinName: String,
    val price: Double,
    val sname : String,
    val pegneg : Boolean,
    val increase : Double,
    val percentage: Double
)

val itemList = listOf(
    Item(R.drawable.btc,"Bitcoin",64364.29,"BTC",false,3174.14,4.9),
    Item(R.drawable.eth,"Ethereum",3063.95,"ETH",false,214.82,7.1),
    Item(R.drawable.binance,"Binance Coin",556.40,"BNB",false,38.80,7.0),
    Item(R.drawable.tether,"Tether ",1.00,"USDT",true,0.00,0.0),
    Item(R.drawable.dollar,"USD Coin",1.00,"USDC",true,0.00,0.0),
)