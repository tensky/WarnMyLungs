package id.tensky.warnmylungs.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import id.tensky.warnmylungs.R
import id.tensky.warnmylungs.models.MapsItemModel
import kotlinx.android.synthetic.main.item_maps.view.*
import kotlin.concurrent.timerTask

class MapsItemRecyclerAdapter(val itemList:List<MapsItemModel>) : RecyclerView.Adapter<MapsItemRecyclerAdapter.MapsItemViewHolder>() {
    lateinit var context:Context
    class MapsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto = itemView.item_maps_foto
        val title = itemView.item_maps_title
        val lokasi = itemView.item_maps_lokasi
        val openNow = itemView.item_maps_openNow
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsItemViewHolder {
        context = parent.context
        return MapsItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_maps, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MapsItemViewHolder, position: Int) {
        val item = itemList[position]
        Log.d("WMLs", ":ada kok ")
        Glide.with(context).load(item.imageLink).apply(RequestOptions.circleCropTransform()).into(holder.foto)
        holder.title.text = item.title
        holder.lokasi.text = item.lokasi
        holder.openNow.text = if(item.openNow) "OPEN" else "CLOSED"
    }

}