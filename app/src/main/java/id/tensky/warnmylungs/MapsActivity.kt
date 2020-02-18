package id.tensky.warnmylungs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.androidnetworking.utils.Utils
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import id.tensky.warnmylungs.adapters.MapsItemRecyclerAdapter
import id.tensky.warnmylungs.callbacks.CallbackAPI
import id.tensky.warnmylungs.models.MapsItemModel
import kotlinx.android.synthetic.main.bottomsheet_maps.*
import org.json.JSONObject
import java.lang.Exception
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val TAG = "WLMs"
    val itemList = mutableListOf<MapsItemModel>()
    val mapsRecyclerAdapter = MapsItemRecyclerAdapter(itemList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        title = "Medical Facility"
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        maps_shimmer.startShimmerAnimation()
        maps_recycler.adapter = mapsRecyclerAdapter
        maps_recycler.layoutManager = LinearLayoutManager(this)

        //Autocomplete
        Places.initialize(this, getString(R.string.google_maps_key))
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.maps_autocomplete) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
            }
            override fun onError(place: Status) {
                Toast.makeText(this@MapsActivity, "Terjadi kesalahan. Silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
        })


    }

    val callback = object : CallbackAPI {
        override fun onCallback(response: JSONObject) {
            maps_shimmer.stopShimmerAnimation()
            maps_shimmer.visibility = View.GONE
            val jsonArray = response.getJSONArray("results")
            for(i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val markerOptions = MarkerOptions()
                Log.d(TAG, "MAPS   : ${jsonObject} ")
                val location = jsonObject.getJSONObject("geometry").getJSONObject("location")
                markerOptions.position(LatLng(location.getDouble("lat"), location.getDouble("lng")))
                markerOptions.icon(getBitmapDescriptor(this@MapsActivity, R.drawable.ic_location_hospital))
                mMap.addMarker(markerOptions)

                var open = false
                try {
                    open = jsonObject.getJSONObject("opening_hours").getBoolean("open_now")
                }catch (e:Exception) {

                }
                val item = MapsItemModel(
                    jsonObject.getString("name"),
                    location.getDouble("lat"),
                    location.getDouble("lng"),
                    open,
                    jsonObject.getString("icon"),
                    jsonObject.getString("vicinity")
                )

                itemList.add(item)
                mapsRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun getBitmapDescriptor(context: Context, id: Int): BitmapDescriptor? {
        val vectorDrawable: Drawable?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            vectorDrawable = context.getDrawable(id)
        } else {
            vectorDrawable = ContextCompat.getDrawable(context, id)
        }
        if (vectorDrawable != null) {
            val w = vectorDrawable.intrinsicWidth
            val h = vectorDrawable.intrinsicHeight

            vectorDrawable.setBounds(0, 0, w, h)
            val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            vectorDrawable.draw(canvas)

            return BitmapDescriptorFactory.fromBitmap(bm);
        }
        return null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMaxZoomPreference(20.0f)
        mMap.setMinZoomPreference(6.0f)
        val jogjaLatLng = LatLng(-7.797068, 110.370529)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jogjaLatLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f))
        googleMap.isMyLocationEnabled = true
        //Fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f))
                TembakAPI.getNearby(callback, it.latitude.toString(), it.longitude.toString(), getString(R.string.google_maps_key))
            }
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
