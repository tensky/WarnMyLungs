package id.tensky.warnmylungs

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.tensky.warnmylungs.callbacks.CallbackAPI
import org.json.JSONObject

class TembakAPI {
    companion object{
        val BASE_URL = "http://192.168.87.89:2300"
        val TAG = "WMLs"
        fun getListKota(callbackAPI: CallbackAPI, index:Int) {
            AndroidNetworking.get("$BASE_URL/beranda/$index")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.d(TAG, "onResponse: $response")
                        callbackAPI.onCallback(response.getJSONObject("data"))
                    }

                    override fun onError(anError: ANError) {
                        Log.d(TAG, "onError: " + anError.errorDetail)
                    }
                })
        }
    }
}