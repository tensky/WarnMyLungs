package id.tensky.warnmylungs

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import id.tensky.warnmylungs.callbacks.CallbackAPI
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class NotificationService : Service() {
    companion object{
        val NOTIFICATION_ID = 1098212
        val NOTIFICATION_CHANNEL = "WMLsChannel"
    }
    var soundEnabled = false
    val TAG = "WMLs"
    lateinit var notificationLayout : RemoteViews
    lateinit var alertNotif : Notification
    lateinit var defaultNotif :Notification
    lateinit var notificationManager : NotificationManager
    lateinit var callback : CallbackAPI
    val index = 3
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        callback = object : CallbackAPI {
            override fun onCallback(response: JSONObject) {
                scheduleit()
                try {
                    if(response.getString("data").toInt() > 100){
                        notificationManager.notify(NOTIFICATION_ID, alertNotif)
                        startForeground(NOTIFICATION_ID, alertNotif)

                    }
                }catch (e:Exception){}
            }
        }
        TembakAPI.getAQI(callback, index)
        scheduleit()
    }

    fun scheduleit(){
        Timer().schedule(object : TimerTask() {
            override fun run() {
                TembakAPI.getAQI(callback, index)
            }
        },20000)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.getBooleanExtra("stopSelf",false)!!){
            stopForeground(true)
            stopSelf()
            Log.d(TAG, "HAHAHAH AKHIRANUA: ")
        }else{
            soundEnabled = intent.getBooleanExtra("soundEnabled", false)

            notificationLayout = RemoteViews(packageName, R.layout.item_notification)
            notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            alertNotif = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(applicationInfo.icon)
                .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(NOTIFICATION_CHANNEL)
                .setCustomBigContentView(notificationLayout)
                .build()

            defaultNotif = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(applicationInfo.icon)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setChannelId(NOTIFICATION_CHANNEL)
                .build()

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                val notifChannel = NotificationChannel(NOTIFICATION_CHANNEL, "Alert", NotificationManager.IMPORTANCE_LOW)
                notifChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(notifChannel)
            }

            notificationManager.notify(NOTIFICATION_ID, defaultNotif)
            startForeground(NOTIFICATION_ID, defaultNotif)
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        Log.d(TAG, "Service on Destroy called: ")
    }

}
