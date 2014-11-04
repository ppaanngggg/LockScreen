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
    private KeyguardManager keyguardManager;
    private KeyguardManager.KeyguardLock keyguardLock;
    Intent lockIntent;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Launcher","onCreate()");
        lockIntent=new Intent(Launcher.this,Setting.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        IntentFilter intentFilter=new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        startActivity(new Intent(Launcher.this,Launcher.class));
    }

    private BroadcastReceiver screenReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Log.d("Launcher","action is "+action);
            if(action.equals("android.intent.action.SCREEN_ON")
                    || action.equals("android.intent.action.SCREEN_OFF")){
                keyguardManager=(KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
                keyguardLock=keyguardManager.newKeyguardLock("");
                keyguardLock.disableKeyguard();
                Log.e("Launcher","close the keyGuard");

                startActivity(lockIntent);
            }
        }
    };

}
