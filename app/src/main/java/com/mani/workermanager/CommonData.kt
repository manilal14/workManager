package com.mani.workermanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class CommonData {

    companion object{

        const val KEY_PAYLOAD = "key_payload"

        fun displayNotification(ctx : Context, title: String, content : String){

            val CHANNEL_ID        = "channel_id"
            val TASK_NOTIFICATION = "task_notification"
            val NOTIFICATION_ID   = 123

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationIntent   = Intent(ctx, NotificationActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent        = PendingIntent.getActivity(ctx,0, notificationIntent,0)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID, TASK_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            var notificationBuilder = NotificationCompat.Builder(ctx,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.star_big_off)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        }
    }


}