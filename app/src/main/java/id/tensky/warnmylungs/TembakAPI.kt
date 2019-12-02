package id.tensky.warnmylungs

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.tensky.warnmylungs.callbacks.CallbackAPI
import org.json.JSONException
import org.json.JSONObject

class TembakAPI {
    companion object{
        val BASE_URL = "https://wmls.azurewebsites.net"
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
        fun getForecast(callbackAPI: CallbackAPI, date:String, index:Int){
            AndroidNetworking.get("$BASE_URL/forecast/$date/$index")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.d(TAG, "onResponse: $response")
                        callbackAPI.onCallback(response)
                    }

                    override fun onError(anError: ANError) {
                        Log.d(TAG, "onError: " + anError.errorDetail)
                    }
                })
        }
        fun postRegisterUser(callbackAPI: CallbackAPI, nik:String, phone:String, name:String, email:String){
            val jsonObject = JSONObject()
            try {
                jsonObject.put("nik", nik)
                jsonObject.put("phone", phone)
                jsonObject.put("name", name)
                jsonObject.put("email", email)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            AndroidNetworking.post("$BASE_URL/user/register")
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        callbackAPI.onCallback(response)
                    }

                    override fun onError(anError: ANError) {
                        Log.d(
                            "Ahnn",
                            "onError:Action " + anError.errorDetail + anError.errorBody
                        )
                    }
                })
        }

        fun getOtpCode(callbackAPI: CallbackAPI, phone:String){
            AndroidNetworking.get("$BASE_URL/user/$phone")
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

        fun postLogin(callbackAPI: CallbackAPI,phone:String){
            val jsonObject = JSONObject()
            try {
                jsonObject.put("phone", phone)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            AndroidNetworking.post("$BASE_URL/user/login")
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        callbackAPI.onCallback(response)
                    }

                    override fun onError(anError: ANError) {
                        Log.d(
                            "Ahnn",
                            "onError:Action " + anError.errorDetail + anError.errorBody
                        )
                    }
                })
        }

        fun getAQI(callbackAPI: CallbackAPI, index:Int){
            AndroidNetworking.get("$BASE_URL/beranda/aqi/$index")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.d(TAG, "onResponse: $response")
                        callbackAPI.onCallback(response)
                    }

                    override fun onError(anError: ANError) {
                        Log.d(TAG, "onError: " + anError.errorDetail)
                    }
                })
        }
    }
}