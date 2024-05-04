package com.example.cryptocurrency

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException



class home_ : Fragment() {

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_home_, container, false)
        val scrollContainer = v.findViewById<LinearLayout>(R.id.scroll_container)
        // Perform network request asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0")
                    .get()
                    .addHeader("X-RapidAPI-Key", "d8364edc31msh8b923d7742a56f8p103aa1jsnfe5c61ef2e81")
                    .addHeader("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                // Update UI on the main thread
                launch(Dispatchers.Main) {
                    val json = responseBody?.let { JSONObject(it) }
                    val data: JSONObject? = json?.optJSONObject("data")
                    val coinsArray = data?.optJSONArray("coins")
                    val firstCoin = coinsArray?.optJSONObject(0)
                    val firstName: String? = firstCoin?.optString("name")
                    coinsArray?.let { array ->
                        val numberOfCoinsToDisplay = minOf(10, array.length())
                        (0 until numberOfCoinsToDisplay).forEach { i ->
                            val coinObject = array.optJSONObject(i)

                            val coinName = coinObject?.optString("name")
                            val coinSymbol = coinObject?.optString("symbol")
                            val imageLink = coinObject?.optString("iconUrl")
                            val coinPriceString = coinObject?.optString("price")
                            val coinPrice: Double? = coinPriceString?.toDoubleOrNull()
                            val formattedPrice = coinPrice?.let { String.format("%.2f", it.toDouble()) }
                            val coinChange = coinObject?.optString("change")
                            val change: Double? = coinChange?.toDoubleOrNull()

                            val cardView = layoutInflater.inflate(R.layout.cardview, scrollContainer, false)
                            val image = cardView.findViewById<ImageView>(R.id.coinimage)
                            val name = cardView.findViewById<TextView>(R.id.coinname)
                            val sname = cardView.findViewById<TextView>(R.id.sname)
                            val value = cardView.findViewById<TextView>(R.id.coinvalue)
                            val profit = cardView.findViewById<TextView>(R.id.coinprofit)


                            name.text = coinName
                            sname.text = coinSymbol
                            Picasso.get().load(imageLink).placeholder(R.drawable.btc).into(image)
                            value.text = "$formattedPrice"
                            profit.text = "$change"

                            scrollContainer.addView(cardView)

                            cardView.setOnClickListener {
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout2,cryptodetails(i))
                                    .commit()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return v
    }
}