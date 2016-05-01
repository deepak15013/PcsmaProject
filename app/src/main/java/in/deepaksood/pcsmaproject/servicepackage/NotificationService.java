package in.deepaksood.pcsmaproject.servicepackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import in.deepaksood.pcsmaproject.mailpackage.AddBookForRent;
import in.deepaksood.pcsmaproject.mailpackage.LendBook;

@SuppressLint("NewApi")
public class NotificationService extends NotificationListenerService {

    private static final String TAG = NotificationService.class.getSimpleName();

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        String pack = sbn.getPackageName();
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Log.i(TAG, "pack: "+pack);
        Log.i(TAG,"title: "+title);
        Log.i(TAG,"text: "+text);

        assert title != null;
        if(pack.equals("com.google.android.gm") && title.equals("GUNJAN SHARMA"))
        {
            Intent intent = new Intent(getApplicationContext(),AddBookForRent.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(pack.equals("com.google.android.gm") && text.contains("BookXchange"))
        {
            String arr[] = text.split(":");
            Intent in=new Intent(getApplicationContext(),LendBook.class);
            in.putExtra("EMAILID",arr[1]);
            in.putExtra("ISBN",arr[2]);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"Notification Removed");
    }
}
