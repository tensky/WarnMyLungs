package id.tensky.warnmylungs

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.tensky.warnmylungs.adapters.ForecastRecyclerAdapter
import id.tensky.warnmylungs.models.ForecastModel
import kotlinx.android.synthetic.main.fragment_forecast.view.*


class ForecastFragment(itemList:MutableList<ForecastModel>, val title:String) : Fragment() {
    lateinit var root:View
    val adapter = ForecastRecyclerAdapter(itemList)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_forecast, container, false)
        root.forecast_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        root.forecast_recycler.adapter = adapter

        return root
    }

}
