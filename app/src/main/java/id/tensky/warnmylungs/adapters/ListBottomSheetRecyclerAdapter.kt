package id.tensky.warnmylungs.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.tensky.warnmylungs.R
import id.tensky.warnmylungs.models.ForecastModel
import kotlinx.android.synthetic.main.item_bottomsheet_list.view.*

class ListBottomSheetRecyclerAdapter(val itemList:List<ForecastModel>) :
    RecyclerView.Adapter<ListBottomSheetRecyclerAdapter.ViewHolder>() {
    lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.bottomsheet_list_time
        val aqi = itemView.bottomsheet_list_aqi
        val temperature = itemView.bottomsheet_list_temperature
        val wind = itemView.bottomsheet_list_wind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottomsheet_list, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.time.text = item.time
        holder.aqi.text = item.aqi.toString()
        holder.temperature.text = item.temperature
        holder.wind.text = item.wind
    }
}