package com.example.malar.kremlinrussiatwiiter.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.malar.kremlinrussiatwiiter.Activities.MainActivity;
import com.example.malar.kremlinrussiatwiiter.Models.TwitterProvider;
import com.example.malar.kremlinrussiatwiiter.R;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by malar on 29.03.2018.
 */

public class NotificationService extends android.app.Service {
    private static long lastId = -1;
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context ctx = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initIdIfNeed();
                while(true){
                    Status lastStatus = getLastStatus();
                    if (lastStatus.getId() != lastId){
                        Intent notificationIntent = new Intent(ctx, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(ctx,
                                0, notificationIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "");
                        builder.setContentIntent(contentIntent)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("KremlinRussia добавил твит")
                                .setContentText(lastStatus.getText())
                                .setAutoCancel(true);

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(1, builder.build());

                        lastId = lastStatus.getId();
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


    private Status getLastStatus(){
        try {
            return TwitterProvider.getInstance().getUserTimeline("KremlinRussia", new Paging(1, 1)).get(0);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initIdIfNeed(){
        if (lastId == -1){
            try {
                lastId = TwitterProvider.getInstance().getUserTimeline("KremlinRussia", new Paging(1, 1)).get(0).getId();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
