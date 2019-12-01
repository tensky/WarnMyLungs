package id.tensky.warnmylungs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import id.tensky.warnmylungs.R
import id.tensky.warnmylungs.adapters.ListAdapter
import id.tensky.warnmylungs.callbacks.CallbackAPI
import id.tensky.warnmylungs.callbacks.CallbackList
import id.tensky.warnmylungs.models.ListModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.json.JSONObject

class ListFragment : Fragment() {
    val itemList = mutableListOf<ListModel>()
    val callbackList = object : CallbackList {
        override fun onItemClicked(index: Int) {
            ListBottomSheetFragment(index).show(childFragmentManager, "BottomList")
        }
    }
    val adapter = ListAdapter(itemList, callbackList)
    val TAG = "WMLs"
    lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_list, container, false)
        root.list_recycler.adapter = adapter
        root.list_recycler.layoutManager = LinearLayoutManager(context)
        itemList.clear()
        for (i in 1..5){
            val callback = object :CallbackAPI{
                override fun onCallback(response: JSONObject) {
                    val item = ListModel(
                        response.getString("namaKota"),
                        response.getString("statusAQI"),
                        response.getString("celcius"),
                        response.getString("humidity"),
                        response.getString("wind"),
                        response.getString("scoreAQI"),
                        when(response.getString("id")){
                            "1"->R.drawable.ic_jakarta
                            "2"->R.drawable.ic_kalteng
                            "3"->R.drawable.ic_yogyakarta
                            "4"->R.drawable.ic_bandung
                            "5"->R.drawable.ic_riau
                            else->0
                        },
                        response.getString("id").toInt()
                    )
                    itemList.add(item)
                    adapter.notifyDataSetChanged()
                }
            }
            TembakAPI.getListKota(callback,i)
        }
        return root
    }
    

}