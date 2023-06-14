package com.example.app_ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 알림 설정을 확인하고, 알림을 생성하거나 취소하는 작업 수행
        val notificationEnabled = getNotificationEnabled(context)
        if (notificationEnabled) {
            showNotification(context)
        } else {
            cancelNotification(context)
        }
    }

    private fun getNotificationEnabled(context: Context): Boolean {
        // 알림 설정 여부를 SharedPreferences 등을 이용하여 가져오는 코드 작성
        val sharedPrefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("NotificationEnabled", false)
    }

    private fun showNotification(context: Context) {
        // 알림 생성하는 코드 작성
        // NotificationCompat.Builder를 사용하여 원하는 형태의 알림을 설정하고, NotificationManagerCompat로 알림을 생성
        val builder = NotificationCompat.Builder(context, "channel_id")
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setSmallIcon(R.drawable.icon_logo_2)
            .setAutoCancel(true) // 알림을 탭하면 자동으로 닫히도록 설정

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(123, builder.build())
    }

    private fun cancelNotification(context: Context) {
        // 알림 취소하는 코드 작성
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(123)
    }
}
