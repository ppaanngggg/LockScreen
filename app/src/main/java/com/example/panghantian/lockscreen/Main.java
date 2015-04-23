package com.example.panghantian.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main extends Activity {
    private final int mRequestCode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button reset_button=(Button)findViewById(R.id.reset);
        reset_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(Main.this, Setting.class), mRequestCode);
                    }
                }
        );
        Button close_lock_button=(Button)findViewById(R.id.close_lock);
        close_lock_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopService(new Intent(Main.this, Launcher.class));
                        TextView textView=(TextView)findViewById(R.id.main_hint);
                        textView.setText(R.string.stop_lock);
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                }
        );
        try {
            FileInputStream fileInputStream=openFileInput("password");
            fileInputStream.close();
            startService(new Intent(Main.this, Launcher.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Main","file not found");
            startActivityForResult(new Intent(Main.this, Setting.class), mRequestCode);
            startService(new Intent(Main.this, Launcher.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==mRequestCode){
            if (resultCode==Activity.RESULT_OK){
                Log.d("Main","subActivity OK");
                startService(new Intent(Main.this,Launcher.class));
            }else if (resultCode==Activity.RESULT_CANCELED){
                Log.d("Main","subActivity cancel");
                finish();
            }
        }
    }

}
