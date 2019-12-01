package id.tensky.warnmylungs

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class NotificationService : Service() {
    companion object{
        val NOTIFICATION_ID = 1098212
        val NOTIFICATION_CHANNEL = "WMLsChannel"
    }
    var soundEnabled = false
    lateinit var notificationLayout : RemoteViews
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        soundEnabled = intent?.getBooleanExtra("soundEnabled", false)!!
        notificationLayout = RemoteViews(packageName, R.layout.item_notification)
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alertNotif = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.ic_exclamation)
            .setStyle(androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setChannelId(NOTIFICATION_CHANNEL)
            .setCustomBigContentView(notificationLayout)
            .build()
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val notifChannel = NotificationChannel(NOTIFICATION_CHANNEL, "Alert", NotificationManager.IMPORTANCE_HIGH)
            notifChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notifChannel)
        }
        notificationManager.notify(NOTIFICATION_ID, alertNotif)
        startForeground(NOTIFICATION_ID, alertNotif)
        return START_REDELIVER_INTENT
    }
}
