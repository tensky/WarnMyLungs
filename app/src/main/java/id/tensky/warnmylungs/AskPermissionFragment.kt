package id.tensky.warnmylungs


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_ask_permission.view.*

/**
 * A simple [Fragment] subclass.
 */
class AskPermissionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_ask_permission, container, false)
        root.permission_turnOnLocation.setOnClickListener{
            startActivity(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            )
            fragmentManager?.popBackStack()
        }

        return root
    }


}
