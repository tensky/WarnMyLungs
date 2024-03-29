package id.tensky.warnmylungs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    lateinit var sharedPreference:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    val TAG = "WMLs"
    companion object{
        const val NOTIFICATION_ACTIVATED = "notif_activated"
        const val SOUND_ACTIVATED = "sound_activated"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedPreference = getSharedPreferences("WMLsSettings", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        settings_unhealthy.isChecked = sharedPreference.getBoolean(NOTIFICATION_ACTIVATED, false)
        settings_sound.isChecked = sharedPreference.getBoolean(SOUND_ACTIVATED, false)
        settings_unhealthy.setOnCheckedChangeListener{ _, checked ->
            editor.putBoolean(NOTIFICATION_ACTIVATED, checked)
            editor.commit()

            val notifIntent = Intent(this, NotificationService::class.java)
            if(checked){
                notifIntent.putExtra("soundEnabled", settings_sound.isChecked)
                startService(notifIntent)
                editor.putBoolean("notasked", true)
                editor.commit()
            }else{
                notifIntent.putExtra("stopSelf", true)
                startService(notifIntent)
            }
        }
        settings_sound.setOnCheckedChangeListener{ _, checked ->
            editor.putBoolean(SOUND_ACTIVATED, checked)
            editor.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}