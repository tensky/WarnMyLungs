package id.tensky.warnmylungs

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.google.android.gms.location.LocationServices
import java.text.DecimalFormat
import java.util.*


class IntroActivity : AppCompatActivity() {
    val REQUEST_LOCATION_PERMISSION = 1
    val TAG = "WMLs"
    lateinit var sharedPreference:SharedPreferences
    lateinit var loginSharedPreference:SharedPreferences
    val decimalFormat = DecimalFormat()
    var gps_enabled = false
    lateinit var locationManager:LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        decimalFormat.maximumFractionDigits = 3
        AndroidNetworking.initialize(this)
        sharedPreference = getSharedPreferences("WMLsSettings", Context.MODE_PRIVATE)
        loginSharedPreference = getSharedPreferences("WMLsLogin", Context.MODE_PRIVATE)
        if(sharedPreference.getBoolean(SettingsActivity.NOTIFICATION_ACTIVATED, false)){
            notifService()
        }
        if(loginSharedPreference.getBoolean("login", false)){
            askForPermission()
        }else{
            askForLogin()
        }
    }

    fun askForLogin(){
        val loginIntent = Intent(this, SignInActivity::class.java)
        startActivity(loginIntent)
        finish()
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
            checkLocationEnabled()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            askForPermission()
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

    fun checkLocationEnabled(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!gps_enabled) {
            AlertDialog.Builder(this@IntroActivity)
                .setMessage("Please Turn On Your GPS")
                .setPositiveButton("Settings"
                ) { _, _ ->
                    startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
            gpsTimer()
        }else{
            getLocation()
        }
    }

    fun gpsTimer(){
        Timer().schedule(object : TimerTask() {
            override fun run() {
                try {
                    gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (gps_enabled) {
                    getLocation()
                    this.cancel()
                }else{
                    Log.d(TAG, "$gps_enabled")
                    gpsTimer()
                }
            }
        },2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            checkLocationEnabled()
        }
    }
}
