package id.tensky.warnmylungs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.tensky.warnmylungs.adapters.ForecastRecyclerAdapter
import id.tensky.warnmylungs.callbacks.CallbackAPI
import id.tensky.warnmylungs.models.ForecastModel
import kotlinx.android.synthetic.main.fragment_forecast.view.*
import org.json.JSONArray
import org.json.JSONObject


class ForecastFragment(val day:String, val date:String, val indexDaerah:Int) : Fragment() {
    lateinit var root:View
    val TAG ="WMLs"
    val itemList = mutableListOf<ForecastModel>()
    val adapter = ForecastRecyclerAdapter(itemList)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemList.clear()
        root = inflater.inflate(R.layout.fragment_forecast, container, false)
        root.forecast_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        root.forecast_recycler.adapter = adapter

        TembakAPI.getForecast(callback, date, indexDaerah)

        return root
    }

    val callback = object : CallbackAPI {
        override fun onCallback(response: JSONObject) {
            val jsonArray = response.getJSONArray("data") as JSONArray
            for(i in 0 until jsonArray.length()){
                val forecastItem = ForecastModel(
                    jsonArray.getJSONObject(i).getString("hour") + ":00",
                    jsonArray.getJSONObject(i).getString("aqi").toInt(),
                    jsonArray.getJSONObject(i).getString("celcius"),
                    jsonArray.getJSONObject(i).getString("wind")
                )
                itemList.add(forecastItem)
                adapter.notifyDataSetChanged()
            }
        }

    }

}
