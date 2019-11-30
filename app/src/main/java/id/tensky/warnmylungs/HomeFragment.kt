package id.tensky.warnmylungs

import android.os.Bundle
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

class HomeFragment : Fragment() {

    private val forecastFragmentList = mutableListOf<ForecastFragment>()
    private val forecastPagerAdapter = fragmentManager?.let { ForecastPagerAdapter(it, forecastFragmentList) }
    var aqi = 0
    val indexDaerah = 0

    lateinit var root:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        root.home_viewPager.adapter = forecastPagerAdapter
        root.home_tabLayout.setupWithViewPager(root.home_viewPager)



        val namaProvinsi = activity?.intent?.getStringExtra("namaProvinsi")

        when{
            namaProvinsi!!.contains("Jakarta")->getHomeData(1)
            namaProvinsi.contains("Kalimantan")->getHomeData(2)
            namaProvinsi.contains("Yogyakarta")->getHomeData(3)
            namaProvinsi.contains("Jawa")->getHomeData(4)
            namaProvinsi.contains("Riau")->getHomeData(5)
        }
        return root
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

    fun updateColor(){
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
                aqi = response.getString("scoreAQI").toInt()
                root.home_kota.text = response.getString("namaKota")
                home_deskripsiKota.text = response.getString("detailKota")
                root.home_aqinumber.text = response.getString("scoreAQI")
                root.home_aqistatus.text = response.getString("statusAQI")
                root.home_pm.text = response.getString("moreDetails")
                root.home_humidity.text = response.getString("humidity")
                root.home_temperature.text = response.getString("celcius") + (0x00B0).toChar()
                root.home_wind.text = response.getString("wind")
                (activity as MainActivity).setActionBarAqi(response.getString("scoreAQI"))
                updateColor()
            }
        }
        TembakAPI.getListKota(callback,index)
    }
}