package id.tensky.warnmylungs


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.tensky.warnmylungs.models.ForecastModel

class ListBottomSheetFragment(val city:String) : BottomSheetDialogFragment(){
    lateinit var root:View
    val itemList  = mutableListOf<ForecastModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.bottomsheet_list, container, false)



        return root
    }


}
