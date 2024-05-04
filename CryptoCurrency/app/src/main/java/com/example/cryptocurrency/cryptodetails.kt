package com.example.cryptocurrency

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


class cryptodetails(i: Int) : Fragment() {
    var index : Int = i
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_cryptodetails, container, false)
        v.findViewById<ImageView>(R.id.imageView6).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout2,home_())
                .commit()
        }
        val graph = v.findViewById<GraphView>(R.id.graph)
        val gridLabelRenderer = graph.gridLabelRenderer
        gridLabelRenderer.isHorizontalLabelsVisible = false
        gridLabelRenderer.isVerticalLabelsVisible = false

        val coinprice = v.findViewById<TextView>(R.id.D_coinPrice)
        val symbol = v.findViewById<TextView>(R.id.D_coinSymbol)
        val profit = v.findViewById<TextView>(R.id.D_coinChange)
        val Avg = v.findViewById<TextView>(R.id.D_GlobalAvg)
        val cap = v.findViewById<TextView>(R.id.D_MarketCap)
        val volume = v.findViewById<TextView>(R.id.D_Volume)
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
                    val coinObject = coinsArray?.optJSONObject(index)
                    val coinName = coinObject?.optString("name")
                    val coinSymbol = coinObject?.optString("symbol")
                    symbol.text = coinSymbol
                    val imageLink = coinObject?.optString("iconUrl")
                    val coinPriceString = coinObject?.optString("price")
                    val coinPrice: Double? = coinPriceString?.toDoubleOrNull()
                    val formattedPrice = coinPrice?.let { String.format("%.2f", it.toDouble()) }
                    coinprice.text =formattedPrice
                    Avg.text = formattedPrice
                    val coinChange = coinObject?.optString("change")
                    val change: Double? = coinChange?.toDoubleOrNull()
                    profit.text = "$change"
                    val marketCapString = coinObject?.optString("marketCap")
                    val marketCapDouble = marketCapString?.toDoubleOrNull() ?: 0.0
                    val formattedMarketCap = if (marketCapDouble >= 1_000_000_000) {
                        val billionValue = marketCapDouble / 1_000_000_000
                        String.format("%.1fB", billionValue)
                    } else {
                        String.format("%.1f", marketCapDouble)
                    }
                    cap.text = formattedMarketCap
                    val vol = coinObject?.optString("24hVolume")
                    val volDouble = vol?.toDoubleOrNull() ?: 0.0
                    val formattedVolume = if (volDouble >= 1_000_000_000) {
                        val billionValue = volDouble / 1_000_000_000
                        String.format("%.1fB", billionValue)
                    } else {
                        String.format("%.1f", volDouble)
                    }
                    volume.text = formattedVolume
                    // Sample sparkline data
                    val sparklineData = coinObject?.optJSONArray("sparkline")
                    val dataPoints = mutableListOf<DataPoint>()
                    val average = dataPoints.map { it.y }.average()
                    // Parse sparkline data into DataPoint objects
                    if (sparklineData != null) {
                        for (i in 0 until sparklineData.length()) {
                            val value = sparklineData.optString(i)
                            if (value != "null") {
                                dataPoints.add(DataPoint(i.toDouble(), value.toDouble()))
                            }
                        }
                    }
                    // Create series from dataPoints
                    val series = LineGraphSeries<DataPoint>(dataPoints.toTypedArray())
                    series.thickness = 8
                    series.color = Color.RED

                    // Add series to the graph
                    graph.addSeries(series)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return v
    }
}