package id.tensky.warnmylungs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.bottomsheet_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        title = "Medical Facility"
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        maps_shimmer.startShimmerAnimation()

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
