package id.tensky.warnmylungs.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.tensky.warnmylungs.R
import id.tensky.warnmylungs.models.ListModel
import kotlinx.android.synthetic.main.item_list.view.*


class ListAdapter(val itemList : List<ListModel>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    lateinit var context:Context
    val TAG = "WMLs"
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.item_list_foto
        val title = itemView.item_list_title
        val deskripsi = itemView.item_list_deskripsi
        val temperature = itemView.item_list_temperature
        val humidity = itemView.item_list_humidity
        val wind = itemView.item_list_wind
        val aqi = itemView.item_list_aqi
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = itemList[position]
        Log.d(TAG, "ItemList: $item ")
        holder.title.text = item.title
        holder.deskripsi.text = item.deskripsi
        holder.temperature.text = item.temperature + (0x00B0).toChar()
        holder.humidity.text = item.humidity
        holder.wind.text = item.wind
        holder.aqi.text = item.aqi
        Glide.with(context).load(item.foto).into(holder.foto)
    }
}