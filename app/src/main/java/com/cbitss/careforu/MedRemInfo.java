package com.cbitss.careforu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MedRemInfo extends AppCompatActivity {
    TextView name,info,interval,unit,freq,startTime,startDate,endDate,title; //TODO: Handle Weekdays
    TextView timers[]= new TextView[6];
    String timers_val[];
    LinearLayout freq_layout,starting_time_layout,weekdays_layout;
    RelativeLayout frequency_rl;
    ArrayList ar;

    //String medname,medinfo,timeunit,weekdays,times,startdate,starttime,enddate;
    //int freq,interval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_rem_info);

        Intent in=getIntent();
        String s=in.getExtras().getString("id");

        DatabaseHandler db=new DatabaseHandler(this);
        ar=db.getReminder(s);

        title = (TextView) findViewById(R.id.title_rem);
        name = (TextView) findViewById(R.id.name);
        info = (TextView) findViewById(R.id.info_rm);
        interval = (TextView) findViewById(R.id.interval);
        unit = (TextView) findViewById(R.id.time_unit);
        freq = (TextView) findViewById(R.id.freq);
        startTime = (TextView) findViewById(R.id.starting_time);
        startDate = (TextView) findViewById(R.id.starting_date);
        endDate = (TextView) findViewById(R.id.ending_date);
        freq_layout = (LinearLayout) findViewById(R.id.freq_layout);
        weekdays_layout = (LinearLayout) findViewById(R.id.weekdays_layout);
        starting_time_layout = (LinearLayout) findViewById(R.id.starting_time_layout);
        frequency_rl = (RelativeLayout) findViewById(R.id.frequency_rl);
        timers[0]=(TextView) findViewById(R.id.time1);
        timers[1]=(TextView) findViewById(R.id.time2);
        timers[2]=(TextView) findViewById(R.id.time3);
        timers[3]=(TextView) findViewById(R.id.time4);
        timers[4]=(TextView) findViewById(R.id.time5);
        timers[5]=(TextView) findViewById(R.id.time6);

        title.setText(ar.get(1).toString());
        name.setText(ar.get(1).toString());
        if(ar.get(2).toString().equals(""))
            info.setText("No instructions added");
        else
        {
            info.setText(ar.get(2).toString());
            info.setTextColor(Color.parseColor("#000000"));
        }
        interval.setText(ar.get(3).toString());
        unit.setText(ar.get(4).toString());
        freq.setText(ar.get(5).toString()+" times");
        timers_val = ar.get(7).toString().split("\\s+");
        startTime.setText(ar.get(8).toString());
        startDate.setText(ar.get(9).toString());
        endDate.setText(ar.get(10).toString());

        if( unit.getText().equals("hours"))
        {
            weekdays_layout.setVisibility(GONE);
            freq_layout.setVisibility(GONE);
            frequency_rl.setVisibility(GONE);
            starting_time_layout.setVisibility(View.VISIBLE);
        }
        else if(unit.getText().equals("weeks"))
        {
            weekdays_layout.setVisibility(View.VISIBLE);
            frequency_rl.setVisibility(View.VISIBLE);
            freq_layout.setVisibility(View.VISIBLE);
            starting_time_layout.setVisibility(GONE);
        }
        else if(unit.getText().equals("days"))
        {
            weekdays_layout.setVisibility(GONE);
            frequency_rl.setVisibility(View.VISIBLE);
            freq_layout.setVisibility(View.VISIBLE);
            starting_time_layout.setVisibility(GONE);
        }

        // Toast.makeText(this,ar.get(6).toString(), Toast.LENGTH_SHORT).show();

        if( unit.getText().equals("weeks") || unit.getText().equals("days"))
        {
            for(int i=0;i<timers_val.length;i++)
            {
                    timers[i].setVisibility(View.VISIBLE);
                    timers[i].setText(timers_val[i]);
            }
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public void delete(View view) {

        DatabaseHandler dh= new DatabaseHandler(this);
        if(dh.deleteReminder(ar.get(0).toString())>0)
        {
            Toast.makeText(this, "Alarm Deleted Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("frag","Med Reminder");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
