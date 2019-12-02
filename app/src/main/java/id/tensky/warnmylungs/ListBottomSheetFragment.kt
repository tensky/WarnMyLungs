package id.tensky.warnmylungs


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.tensky.warnmylungs.adapters.ListBottomSheetRecyclerAdapter
import id.tensky.warnmylungs.callbacks.CallbackAPI
import id.tensky.warnmylungs.models.ForecastModel
import kotlinx.android.synthetic.main.fragment_bottomsheet_list.*
import kotlinx.android.synthetic.main.fragment_bottomsheet_list.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ListBottomSheetFragment(val indexDaerah:Int) : BottomSheetDialogFragment(){
    lateinit var root:View
    val itemList  = mutableListOf<ForecastModel>()
    val adapter = ListBottomSheetRecyclerAdapter(itemList)
    val TAG = "WMLs"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_bottomsheet_list, container, false)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val day = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"))
        root.bottomsheet_list_day.text = dayFormat.format(day.time)
        root.bottomsheet_list_recyclerView.adapter = adapter
        root.bottomsheet_list_recyclerView.layoutManager = LinearLayoutManager(context)
        root.bottomsheet_list_recyclerView.setHasFixedSize(true)
        TembakAPI.getForecast(callback, dateFormat.format(day.time), indexDaerah)

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
