package com.shater.invoices.util

import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import com.shater.invoices.R

object Notifications {
    private const val CHANNEL = "shater_events"
    fun createChannel(context: Context) { if (android.os.Build.VERSION.SDK_INT >= 26) context.getSystemService(NotificationManager::class.java).createNotificationChannel(NotificationChannel(CHANNEL, "تنبيهات شاطر للفواتير", NotificationManager.IMPORTANCE_DEFAULT)) }
    fun show(context: Context, title: String, text: String) { val notification = NotificationCompat.Builder(context, CHANNEL).setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(title).setContentText(text).setAutoCancel(true).build(); context.getSystemService(NotificationManager::class.java).notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notification) }
}
