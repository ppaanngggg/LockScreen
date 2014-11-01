package com.example.panghantian.lockscreen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Setting extends Activity implements View.OnClickListener{
    TextView text;
    Button num_1;
    Button num_2;
    Button num_3;
    Button num_4;
    Button num_5;
    Button num_6;
    Button num_7;
    Button num_8;
    Button num_9;
    Button num_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        text=(TextView)findViewById(R.id.text);
        num_1=(Button)findViewById(R.id.num_1);
        num_2=(Button)findViewById(R.id.num_2);
        num_3=(Button)findViewById(R.id.num_3);
        num_4=(Button)findViewById(R.id.num_4);
        num_5=(Button)findViewById(R.id.num_5);
        num_6=(Button)findViewById(R.id.num_6);
        num_7=(Button)findViewById(R.id.num_7);
        num_8=(Button)findViewById(R.id.num_8);
        num_9=(Button)findViewById(R.id.num_9);
        num_0=(Button)findViewById(R.id.num_0);
        num_1.setOnClickListener(this);
        num_2.setOnClickListener(this);
        num_3.setOnClickListener(this);
        num_4.setOnClickListener(this);
        num_5.setOnClickListener(this);
        num_6.setOnClickListener(this);
        num_7.setOnClickListener(this);
        num_8.setOnClickListener(this);
        num_9.setOnClickListener(this);
        num_0.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
    public boolean onTouchEvent(MotionEvent event){
        Log.d("pressure : ", Float.toString(event.getPressure()));
        Log.d("size : ",Float.toString(event.getSize()));
        return true;
    }

    @Override
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.num_1:
                text.setText(text.getText()+"1");
                break;
            case R.id.num_2:
                text.setText(text.getText()+"2");
                break;
            case R.id.num_3:
                text.setText(text.getText()+"3");
                break;
            case R.id.num_4:
                text.setText(text.getText()+"4");
                break;
            case R.id.num_5:
                text.setText(text.getText()+"5");
                break;
            case R.id.num_6:
                text.setText(text.getText()+"6");
                break;
            case R.id.num_7:
                text.setText(text.getText()+"7");
                break;
            case R.id.num_8:
                text.setText(text.getText()+"8");
                break;
            case R.id.num_9:
                text.setText(text.getText()+"9");
                break;
            case R.id.num_0:
                text.setText(text.getText()+"0");
                break;
        }
    }
}
