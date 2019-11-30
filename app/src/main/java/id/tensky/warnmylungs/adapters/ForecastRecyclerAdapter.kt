package id.tensky.warnmylungs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.tensky.warnmylungs.R
import id.tensky.warnmylungs.models.ForecastModel
import kotlinx.android.synthetic.main.item_forecast.view.*

class ForecastRecyclerAdapter(val itemList:List<ForecastModel>) : RecyclerView.Adapter<ForecastRecyclerAdapter.ForecastViewHolder>() {
    lateinit var context: Context

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.forecast_time
        val aqi = itemView.forecast_aqi
        val temperature = itemView.forecast_temperature
        val wind = itemView.forecast_wind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        context = parent.context
        return ForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.item_forecast, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = itemList[position]
        holder.time.text = item.time
        holder.aqi.text = item.aqi.toString()
        holder.temperature.text = item.temperature
        holder.wind.text = item.wind
    }
}