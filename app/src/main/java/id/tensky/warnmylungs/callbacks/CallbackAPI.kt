package id.tensky.warnmylungs.callbacks

import org.json.JSONObject

interface CallbackAPI {
    fun onCallback(response:JSONObject)
}