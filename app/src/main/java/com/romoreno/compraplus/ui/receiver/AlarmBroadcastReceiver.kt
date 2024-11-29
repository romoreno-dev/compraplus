package com.romoreno.compraplus.ui.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.romoreno.compraplus.R
import com.romoreno.compraplus.ui.main.MainActivity

/**
 * Crea notificacion local que recuerda al usuario que debe realizar una compra en la hora dada
 * que el haya programado previamente
 *
 * @author Roberto Moreno
 */
class AlarmBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 31416
        const val CHANNEL_ID = "GROCERY_LIST_CHANNEL"
        const val NOTIFICATION_TITLE = "NOTIFICATION_TITLE"
        const val NOTIFICATION_BODY = "NOTIFICATION_BODY"
    }

    override fun onReceive(context: Context, intent: Intent) {
        createNotification(context, intent)
    }

    private fun createNotification(context: Context, entryIntent: Intent) {
        val notificationTitle = entryIntent.getStringExtra(NOTIFICATION_TITLE)
        val notificationBody = entryIntent.getStringExtra(NOTIFICATION_BODY)

        val intent = Intent(context, MainActivity::class.java).apply {
            // Asi se evita la aparición de instancias nuevas de la aplicacion al clickar
            // en la notificacion
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Este flag es necesario para notificaciones superiores en API 23
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notificationCompat = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_compraplus_splash)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificationCompat)
    }

}
