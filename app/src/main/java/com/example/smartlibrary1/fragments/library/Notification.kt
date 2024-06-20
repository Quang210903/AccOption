package com.example.smartlibrary1.fragments.library

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.smartlibrary1.R
import com.example.smartlibrary1.activities.LoginResisterActivities
import com.example.smartlibrary1.activities.mylibraryActivity


const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver()
{

    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context, intent: Intent)
    {
        val title = intent.getStringExtra(titleExtra)
        val message = intent.getStringExtra(messageExtra)
        val uniqueID = intent.getIntExtra("uniqueID",-1)
        val mainIntent = Intent(context, LoginResisterActivities::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setLargeIcon(
                BitmapFactory.decodeResource(context.resources,
                    R.drawable.ic_notifications))
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(uniqueID, notification)
    }


}