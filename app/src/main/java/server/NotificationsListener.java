package server;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;

import solutions.lhdev.app.app.InicioActivity;
import solutions.lhdev.app.app.R;

/**
 * Created by JuanIgnacio on 03/06/2017.
 */

public class NotificationsListener extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Intent intent = new Intent("unique_name");

        //send broadcast
        this.getApplicationContext().sendBroadcast(intent);

        /*
        This is usefull only if you want the Notification pushed when application is active.
        If application not active, Push will be builded based on the information received over
        internet.
        */

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.go);
        mBuilder.setContentTitle(((Bundle) data.get("notification")).get("title").toString());
        mBuilder.setContentText(((Bundle) data.get("notification")).get("body").toString());
        mBuilder.setAutoCancel(true);
        //Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, InicioActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InicioActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
