package id.tensky.warnmylungs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.google.android.gms.location.LocationServices

class IntroActivity : AppCompatActivity() {
    val REQUEST_LOCATION_PERMISSION = 1
    val TAG = "WMLs"
    lateinit var sharedPreference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        askForPermission()
        AndroidNetworking.initialize(this)
        sharedPreference = getSharedPreferences("WMLsSettings", Context.MODE_PRIVATE)
        if(sharedPreference.getBoolean(SettingsActivity.NOTIFICATION_ACTIVATED, false)){
            notifService()
        }
    }

    fun notifService(){
        val notifIntent = Intent(this, NotificationService::class.java)
        notifIntent.putExtra("soundEnabled", sharedPreference.getBoolean(SettingsActivity.SOUND_ACTIVATED, false))
        startService(notifIntent)
    }

    fun askForPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }else{
            getLocation()
        }
    }

    fun getLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it == null){
                askForPermission()
                return@addOnSuccessListener
            }
            val addressList = Geocoder(this).getFromLocation(it.latitude, it.longitude, 1)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("namaProvinsi", addressList[0].adminArea)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOCATION_PERMISSION && resultCode == Activity.RESULT_OK){
            getLocation()
        }
    }
}
