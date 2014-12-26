package com.example.panghantian.lockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.IBinder;
import android.os.SystemClock;
import android.sax.RootElement;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by panghantian on 14/11/4.
 */
public class Launcher extends Service implements View.OnClickListener,View.OnTouchListener{
    WindowManager windowManager;
    WindowManager.LayoutParams layoutparams;
    RelativeLayout relativeLayout;
    TextView text;
    String password;
    String hint;
    Vector<Long> hold_time;
    long down_time[];
    Vector<Vector<Float>> pressure;
    Vector pressure_num[];
    Vector<Vector<Float>> size;
    Vector size_num[];
    Button nums[]=null;
    ImageButton clear;
    ImageButton ok;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("Launcher","onCreate()");

        KeyguardManager.KeyguardLock keyguardLock;
        KeyguardManager keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        keyguardLock=keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();

        IntentFilter intentFilter=new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenReceiver, intentFilter);
        setUpLayout();
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
                launchLock();
            }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                wasScreenOn=true;
            }else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                launchLock();
            }
        }
    };

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int spTpPx(int sp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(sp * displayMetrics.scaledDensity);
    }

    private void setUpLayout(){


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutparams= new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.RGBA_8888
        );

        nums=new Button[10];
        relativeLayout = new RelativeLayout(this);
        relativeLayout.setBackgroundResource(R.drawable.back);
        int marginPix=dpToPx(-16);
        int paddingPix=dpToPx(20);
        RelativeLayout.LayoutParams params[]=new RelativeLayout.LayoutParams[10];

        for (int i=0;i<10;i++){
            params[i]=new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params[i].bottomMargin=marginPix;
            nums[i]=new Button(this);
            nums[i].setId(i);
            nums[i].setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
            nums[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            nums[i].setTypeface(null, Typeface.NORMAL);
            nums[i].setTextColor(Color.parseColor("#a0333333"));
            nums[i].setBackgroundColor(Color.parseColor("#00000000"));
            nums[i].setOnClickListener(this);
            nums[i].setOnTouchListener(this);
        }
        params[2].addRule(RelativeLayout.CENTER_IN_PARENT);
        nums[2].setText(R.string.num_2);
        params[5].addRule(RelativeLayout.CENTER_HORIZONTAL);
        params[5].addRule(RelativeLayout.BELOW, 2);
        nums[5].setText(R.string.num_5);
        params[1].addRule(RelativeLayout.LEFT_OF, 2);
        params[1].addRule(RelativeLayout.ABOVE,5);
        nums[1].setText(R.string.num_1);
        params[3].addRule(RelativeLayout.RIGHT_OF, 2);
        params[3].addRule(RelativeLayout.ABOVE, 5);
        nums[3].setText(R.string.num_3);
        params[4].addRule(RelativeLayout.LEFT_OF, 2);
        params[4].addRule(RelativeLayout.BELOW,2);
        nums[4].setText(R.string.num_4);
        params[6].addRule(RelativeLayout.RIGHT_OF, 2);
        params[6].addRule(RelativeLayout.BELOW, 2);
        nums[6].setText(R.string.num_6);
        params[7].addRule(RelativeLayout.LEFT_OF, 5);
        params[7].addRule(RelativeLayout.BELOW,5);
        nums[7].setText(R.string.num_7);
        params[8].addRule(RelativeLayout.BELOW, 5);
        params[8].addRule(RelativeLayout.RIGHT_OF,4);
        nums[8].setText(R.string.num_8);
        params[9].addRule(RelativeLayout.BELOW, 5);
        params[9].addRule(RelativeLayout.RIGHT_OF,5);
        nums[9].setText(R.string.num_9);
        params[0].addRule(RelativeLayout.BELOW, 8);
        params[0].addRule(RelativeLayout.RIGHT_OF,7);
        nums[0].setText(R.string.num_0);

        RelativeLayout.LayoutParams clear_param=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        clear_param.addRule(RelativeLayout.BELOW,8);
        clear_param.addRule(RelativeLayout.LEFT_OF, 8);
        clear_param.setMargins(0, dpToPx(6), dpToPx(12), 0);
        clear=new ImageButton(this);
        clear.setId(R.id.clear);
        clear.setImageResource(R.drawable.ic_action_cancel);
        clear.setBackgroundColor(Color.parseColor("#00000000"));
        clear.setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
        clear.setLayoutParams(clear_param);
        clear.setOnClickListener(this);

        RelativeLayout.LayoutParams ok_param=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        ok_param.addRule(RelativeLayout.BELOW,8);
        ok_param.addRule(RelativeLayout.RIGHT_OF, 8);
        ok_param.setMargins(dpToPx(12), dpToPx(6), 0, 0);
        ok=new ImageButton(this);
        ok.setId(R.id.ok);
        ok.setImageResource(R.drawable.ic_action_accept);
        ok.setBackgroundColor(Color.parseColor("#00000000"));
        ok.setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
        ok.setLayoutParams(ok_param);
        ok.setOnClickListener(this);

        RelativeLayout.LayoutParams text_param=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        text_param.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text_param.addRule(RelativeLayout.ABOVE,2);
        text=new TextView(this);
        text.setId(R.id.text);
        text.setLayoutParams(text_param);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        text.setTypeface(null, Typeface.NORMAL);
        text.setTextColor(Color.parseColor("#a0333333"));
        text.setBackgroundColor(Color.parseColor("#00000000"));
        text.setPadding(dpToPx(30),dpToPx(30),dpToPx(30),dpToPx(30));

        for (int i=0;i<10;i++){
            nums[i].setLayoutParams(params[i]);
        }
        for (int i=0;i<10;i++){
            relativeLayout.addView(nums[i]);
        }
        relativeLayout.addView(clear);
        relativeLayout.addView(ok);
        relativeLayout.addView(text);
    }

    private void launchLock(){
        password="";
        hint="";
        hold_time=new Vector<Long>();
        down_time=new long[10];
        pressure=new Vector<Vector<Float>>();
        pressure_num=new Vector[10];
        size=new Vector<Vector<Float>>();
        size_num=new Vector[10];

        text.setText(hint);

        for (int i=0;i<10;i++){
            pressure_num[i]=new Vector<Float>();
            size_num[i]=new Vector<Float>();
        }

        try {
            windowManager.addView(relativeLayout,layoutparams);
        }catch (RuntimeException e){
//            e.printStackTrace();
            Log.d("window manager","layout has already been added");
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==R.id.ok){
            Log.d("launcher","ok");
            Log.d("password",password);
            Log.d("hold time",hold_time.toString());
            Log.d("pressure",pressure.toString());
            Log.d("size",size.toString());
            windowManager.removeView(relativeLayout);
        }else if (id==R.id.clear){
            text.setText("请重新输入");
            hint="";
            password="";
            hold_time=new Vector<Long>();
            pressure=new Vector<Vector<Float>>();
            size=new Vector<Vector<Float>>();
        }else {
            text.setText(hint+="*");
            password+=Integer.toString(id);
            Log.d("password",password);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id=view.getId();
        if(motionEvent.getActionMasked()==MotionEvent.ACTION_DOWN){
            down_time[id]= SystemClock.uptimeMillis();
            pressure_num[id]=new Vector<Float>();
            size_num[id]=new Vector<Float>();
        }else if(motionEvent.getActionMasked()==MotionEvent.ACTION_UP){
            hold_time.add(SystemClock.uptimeMillis()-down_time[id]);
            pressure.add((Vector<Float>)pressure_num[id]);
            size.add((Vector<Float>)size_num[id]);
        }
        pressure_num[id].add(motionEvent.getPressure());
        size_num[id].add(motionEvent.getSize());
        return false;
    }
}
