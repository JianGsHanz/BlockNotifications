package com.zyh.blocknotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.zyh.blocknotifications.db.DbManager;

import org.greenrobot.eventbus.EventBus;


public class NotificationMonitorService extends NotificationListenerService {

    private static final String TAG = "NMService";

    private PowerManager.WakeLock wakeLock;
    private DbManager dbManager;


    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "启动服务", Toast.LENGTH_LONG).show();
        dbManager = new DbManager(this);
        NotificationManager mNM = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNM != null) {
            NotificationChannel mNotificationChannel = mNM.getNotificationChannel("id");
            if (mNotificationChannel == null) {
                mNotificationChannel = new NotificationChannel("id", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
                mNotificationChannel.setDescription("Notificationddd");
                mNM.createNotificationChannel(mNotificationChannel);
            }
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, "id");//

        nb.setContentTitle("Notification").setTicker("Notification").setSmallIcon(R.mipmap.ic_launcher);
        nb.setContentText("内容");
        //nb.setContent(new RemoteViews(getPackageName(),R.layout.layout));
        nb.setWhen(System.currentTimeMillis());
        Notification notification = nb.build();
        startForeground(0, notification);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //保持cpu一直运行，不管屏幕是否黑屏
        if (pm != null && wakeLock == null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.acquire();
        }


    }


    public void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        Intent localIntent = new Intent();
        localIntent.setClass(this, NotificationMonitorService.class);
        startService(localIntent);
    }


    public void onNotificationPosted(StatusBarNotification sbn) {

        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        String content = "";
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            Log.e("包名：",sbn.getPackageName()+"标题:"+title+"内容:"+content);
            if (!content.contains("搜狗输入法")) {
//                Toast.makeText(this, sbn.getPackageName() + "标题:" + title + "内容:" + content, Toast.LENGTH_SHORT).show();
                dbManager.insert(sbn.getPackageName(),title,content);
                EventBus.getDefault().postSticky(new MsgEvent("hi"));
            }
        }

    }


    public void onNotificationRemoved(StatusBarNotification paramStatusBarNotification) {
        if (Build.VERSION.SDK_INT >= 19) {
            Bundle localObject = paramStatusBarNotification.getNotification().extras;
            String pkgName = paramStatusBarNotification.getPackageName();
            String title = localObject.getString("android.title");
            String text = (localObject).getString("android.text");
            Log.e(TAG, "Notification removed [" + pkgName + "]:" + title + " & " + text);
        }
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return START_NOT_STICKY;
    }


}