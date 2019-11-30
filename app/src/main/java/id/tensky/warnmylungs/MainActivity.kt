package id.tensky.warnmylungs

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentTransaction: FragmentTransaction
    private val homeFragment = HomeFragment()
    private val listFragment = ListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val actionDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(actionDrawerToggle)
        actionDrawerToggle.syncState()
        fragmentTransaction.replace(R.id.main_frame, homeFragment)
        fragmentTransaction.commit()
        appbar_drawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
         navigationView.setNavigationItemSelectedListener {
             when(it.itemId){
                 R.id.nav_home->{
                     fragmentTransaction = supportFragmentManager.beginTransaction()
                     fragmentTransaction.replace(R.id.main_frame, homeFragment)
                     fragmentTransaction.commit()
                     drawerLayout.closeDrawer(GravityCompat.START)
                     return@setNavigationItemSelectedListener true
                 }
                 R.id.nav_list->{
                     fragmentTransaction = supportFragmentManager.beginTransaction()
                     fragmentTransaction.replace(R.id.main_frame, listFragment)
                     fragmentTransaction.commit()
                     drawerLayout.closeDrawer(GravityCompat.START)
                     return@setNavigationItemSelectedListener true
                 }
                 R.id.nav_location->{
                     startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                     drawerLayout.closeDrawer(GravityCompat.START)
                     return@setNavigationItemSelectedListener true
                 }
                 R.id.nav_settings->{
                     startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                     drawerLayout.closeDrawer(GravityCompat.START)
                     return@setNavigationItemSelectedListener true
                 }
                 R.id.nav_report->{
                     fragmentTransaction = supportFragmentManager.beginTransaction()
                     fragmentTransaction.replace(R.id.main_frame, SendReportFragment())
                     fragmentTransaction.commit()
                     drawerLayout.closeDrawer(GravityCompat.START)
                     return@setNavigationItemSelectedListener true
                 }
                 else->return@setNavigationItemSelectedListener true
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun setActionBarAqi(aqi:String){
        appbar_aqiIndex.text = aqi + " AQI"
    }
}
