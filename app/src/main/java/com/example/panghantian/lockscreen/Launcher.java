package com.example.panghantian.lockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by panghantian on 14/11/4.
 */
public class Launcher extends Service {

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        Log.d("Launcher","onCreate()");

        KeyguardManager.KeyguardLock keyguardLock;
        KeyguardManager keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        keyguardLock=keyguardManager.newKeyguardLock("Lock");
        keyguardLock.disableKeyguard();

        IntentFilter intentFilter=new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenReceiver,intentFilter);
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(screenReceiver);
        startActivity(new Intent(Launcher.this,Launcher.class));
        super.onDestroy();

    }

    private BroadcastReceiver screenReceiver=new BroadcastReceiver() {
        public boolean wasScreenOn=true;
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.d("Launcher","action is "+action);

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                wasScreenOn=false;
                Intent intent1=new Intent(context,Lock.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                wasScreenOn=true;
                Intent intent1=new Intent(context,Lock.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                Intent intent1=new Intent(context,Lock.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }
    };

}
