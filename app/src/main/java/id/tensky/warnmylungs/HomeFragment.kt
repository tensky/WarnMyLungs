package id.tensky.warnmylungs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import id.tensky.warnmylungs.adapters.ForecastPagerAdapter
import id.tensky.warnmylungs.callbacks.CallbackAPI
import id.tensky.warnmylungs.models.ListModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val forecastFragmentList = mutableListOf<ForecastFragment>()
    private lateinit var forecastPagerAdapter : ForecastPagerAdapter
    var indexDaerah = 0

    lateinit var root:View
    val TAG = "WMLs"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        forecastFragmentList.clear()
        forecastPagerAdapter = ForecastPagerAdapter(childFragmentManager, forecastFragmentList)
        root.home_viewPager.adapter = forecastPagerAdapter
        root.home_tabLayout.setupWithViewPager(root.home_viewPager)
        root.home_shimmer.startShimmerAnimation()
        val namaProvinsi = activity?.intent?.getStringExtra("namaProvinsi")

        when{
            namaProvinsi!!.contains("Jakarta")->{
                getHomeData(1)
                indexDaerah = 1
            }
            namaProvinsi.contains("Kalimantan")->{
                getHomeData(2)
                indexDaerah = 2
            }
            namaProvinsi.contains("Yogyakarta")->{
                getHomeData(3)
                indexDaerah = 3
            }
            namaProvinsi.contains("Jawa")->{
                getHomeData(4)
                indexDaerah = 4
            }
            namaProvinsi.contains("Riau")->{
                getHomeData(5)
                indexDaerah = 5
            }
            else-> {
                getHomeData(3)
                indexDaerah = 3
            }
        }

        makeForecast()
        return root
    }

    fun makeForecast(){
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        val day = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"))
        forecastFragmentList.add(ForecastFragment(dayFormat.format(day.time), dateFormat.format(day.time), indexDaerah))
        day.add(Calendar.DATE, 1)
        forecastFragmentList.add(ForecastFragment(dayFormat.format(day.time), dateFormat.format(day.time), indexDaerah))
        day.add(Calendar.DATE, 1)
        forecastFragmentList.add(ForecastFragment(dayFormat.format(day.time), dateFormat.format(day.time), indexDaerah))
        forecastPagerAdapter.notifyDataSetChanged()
    }

    fun redCondition(){
        root.home_conditionLogo.setImageResource(R.drawable.ic_face_red)
        root.home_conditionLogo.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxred))
        root.home_informationLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxlightred))
        root.home_aqinumber.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkred))
        root.home_aqitext.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkred))
        root.home_aqistatus.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkred))
        root.home_pm.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkred))
        root.home_aqistatus.text = "Unhealthy"
    }

    fun yellowCondition(){
        root.home_conditionLogo.setImageResource(R.drawable.ic_face_yellow)
        root.home_conditionLogo.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxyellow))
        root.home_informationLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxlightyellow))
        root.home_aqinumber.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkyellow))
        root.home_aqitext.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkyellow))
        root.home_aqistatus.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkyellow))
        root.home_pm.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkyellow))
        root.home_aqistatus.text = "Moderate"
    }
    fun greenCondition(){
        root.home_conditionLogo.setImageResource(R.drawable.ic_face_green)
        root.home_conditionLogo.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxgreen))
        root.home_informationLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.boxlightgreen))
        root.home_aqinumber.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkgreen))
        root.home_aqitext.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkgreen))
        root.home_aqistatus.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkgreen))
        root.home_pm.setTextColor(ContextCompat.getColor(context!!, R.color.boxdarkgreen))
        root.home_aqistatus.text = "Great"
    }

    fun updateColor(aqi:Int){
        when{
            aqi < 50 -> {
                greenCondition()
            }
            aqi <= 100 -> {
                yellowCondition()
            }
            aqi > 100 -> {
                redCondition()
            }
        }
    }

    fun getHomeData(index:Int){
        val callback = object : CallbackAPI {
            override fun onCallback(response: JSONObject) {
                root.home_shimmer.stopShimmerAnimation()
                root.home_shimmer.visibility = View.GONE
                root.home_infoCardView.visibility = View.VISIBLE
                root.home_kota.text = response.getString("namaKota")
                root.home_deskripsiKota.text = response.getString("detailKota")
                root.home_aqinumber.text = response.getString("scoreAQI")
                root.home_aqistatus.text = response.getString("statusAQI")
                root.home_pm.text = response.getString("moreDetails")
                root.home_humidity.text = response.getString("humidity")
                root.home_temperature.text = response.getString("celcius") + (0x00B0).toChar()
                root.home_wind.text = response.getString("wind")
                (activity as MainActivity).setActionBarAqi(response.getString("scoreAQI"))
                updateColor(response.getString("scoreAQI").toInt())
            }
        }
        TembakAPI.getListKota(callback,index)
    }
}