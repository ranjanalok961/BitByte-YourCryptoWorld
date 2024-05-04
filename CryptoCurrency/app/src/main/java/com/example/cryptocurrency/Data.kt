package com.example.cryptocurrency

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class Data {


    private val client = OkHttpClient()

    private val request = Request.Builder()
        .url("https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0")
        .get()
        .addHeader("X-RapidAPI-Key", "d8364edc31msh8b923d7742a56f8p103aa1jsnfe5c61ef2e81")
        .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
        .build()

    private val response = client.newCall(request).execute()


    // Access the JSON response as a string
    private val responseBody = response.body?.string()

    private val json = responseBody?.let { JSONObject(it) }
    private val data: JSONObject? = json?.optJSONObject("data")
    private val coinsArray = data?.optJSONArray("coins")
    private val firstCoin = coinsArray?.optJSONObject(0)
    val firstName: String? = firstCoin?.optString("name")


}