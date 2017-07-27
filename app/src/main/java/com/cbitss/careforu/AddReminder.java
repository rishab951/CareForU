package com.cbitss.careforu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.GONE;

public class AddReminder extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener,FragmentReminder.OnFragmentInteractionListener {

    Spinner interval,time_unit,times;
    LinearLayout freq_layout,starting_time_layout,weekdays_layout;
    RelativeLayout frequency_rl;
    ImageView back;
    TextView timers[] = new TextView[6];
    TextView starting_time,starting_date,ending_date,medname,medinfo;
    CheckBox weekday; //TODO : Handle Weekday

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // weekday=(CheckBox) findViewById(R.id.weekday);
        back=(ImageView) findViewById(R.id.back_add_reminder);
        frequency_rl = (RelativeLayout) findViewById(R.id.frequency_rl);
        medinfo=(TextView)findViewById(R.id.medinf);
        medname=(TextView)findViewById(R.id.medname);
        interval = (Spinner)findViewById(R.id.interval);
        times = (Spinner)findViewById(R.id.times);
        time_unit = (Spinner)findViewById(R.id.time_unit);
        freq_layout = (LinearLayout) findViewById(R.id.freq_layout);
        weekdays_layout = (LinearLayout) findViewById(R.id.weekdays_layout);
        starting_time_layout = (LinearLayout) findViewById(R.id.starting_time_layout);
        starting_time = (TextView) findViewById(R.id.starting_time);
        starting_date =(TextView) findViewById(R.id.starting_date);
        ending_date = (TextView) findViewById(R.id.ending_date);
        timers[0]=(TextView) findViewById(R.id.time1);
        timers[1]=(TextView) findViewById(R.id.time2);
        timers[2]=(TextView) findViewById(R.id.time3);
        timers[3]=(TextView) findViewById(R.id.time4);
        timers[4]=(TextView) findViewById(R.id.time5);
        timers[5]=(TextView) findViewById(R.id.time6);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.interval, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        interval.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.time_unit, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_unit.setAdapter(adapter2);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.times, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        times.setAdapter(adapter3);

        //Settings listeners
        times.setOnItemSelectedListener(this);
        time_unit.setOnItemSelectedListener(this);
        interval.setOnItemSelectedListener(this);
        starting_date.setOnClickListener(this);
        ending_date.setOnClickListener(this);
        starting_time.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Setting starting time,date and ending date
        Calendar calendar= Calendar.getInstance();
        starting_time.setText(String.format("%02d",calendar.get(Calendar.HOUR))+":"+
                String.format("%02d",calendar.get(Calendar.MINUTE)));
        starting_date.setText(String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH))+"/"+
                String.format("%02d",calendar.get(Calendar.MONTH)+1)+"/" + String.format("%04d",calendar.get(Calendar.YEAR)));
        ending_date.setText(String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH))+"/"+
                String.format("%02d",calendar.get(Calendar.MONTH)+1)+"/" + String.format("%04d",calendar.get(Calendar.YEAR)+1));

    }

    public String intToTime(int num)
    {
        return (String.format("%02d", num/60)+":"+ String.format("%02d", num%60));
    }

    ArrayList ar;
    static int hr;
    @Override
    public void onClick(View v) {
        if(v==timers[0]||v==timers[1]||v==timers[2]||v==timers[3]||v==timers[4]||v==timers[5]||v==starting_time)
        {
            final TextView clickedView = (TextView) v;
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    clickedView.setText(String.format("%02d", selectedHour)+":"+ String.format("%02d", selectedMinute));
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time\n");
            mTimePicker.show();
        }

        else {
            if (v == starting_date || v == ending_date) {
                final TextView clickedView = (TextView) v;
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int monthOfYear, int dayOfMonth) {
                        clickedView.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear + 1) + "/"
                                + String.format("%04d", year));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.time_unit:
                String time_unit = parent.getItemAtPosition(position).toString();
                if( time_unit.equals("hours"))
                {
                    weekdays_layout.setVisibility(GONE);
                    freq_layout.setVisibility(GONE);
                    frequency_rl.setVisibility(GONE);
                    starting_time_layout.setVisibility(View.VISIBLE);
                }
                else if(time_unit.equals("weeks"))
                {
                    weekdays_layout.setVisibility(View.VISIBLE);
                    frequency_rl.setVisibility(View.VISIBLE);
                    freq_layout.setVisibility(View.VISIBLE);
                    starting_time_layout.setVisibility(GONE);
                }
                else
                {
                    weekdays_layout.setVisibility(GONE);
                    frequency_rl.setVisibility(View.VISIBLE);
                    freq_layout.setVisibility(View.VISIBLE);
                    starting_time_layout.setVisibility(GONE);
                }
                break;

            case R.id.times:
                int times = position+1;
                int time_int ;
                for(int i=0;i<times;i++)
                {
                    time_int = 1440*i/times;
                    timers[i].setVisibility(View.VISIBLE);
                    timers[i].setOnClickListener(this);
                    timers[i].setText(intToTime(time_int));
                }
                for(int i=5;i>position;i--)
                {
                    timers[i].setVisibility(GONE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /*interval.setSelected(false);
        time_unit.setSelected(false);*/
    }

    public void done(View view) {
        //If Name is missing, prompt User
        if(medname.getText().toString().equals(""))
        {
            medname.requestFocus();
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        ar=new ArrayList();
        int NoOfTimes=Integer.parseInt((times.getSelectedItem().toString()).substring(0,1));

        DatabaseHandler dh= new DatabaseHandler(this);
        int count=dh.getRemindersCount();

        if( NoOfTimes>1)
            count=count+NoOfTimes;
        else
            count=count+1;

        ar.add(count);
        ar.add(medname.getText().toString());
        ar.add(medinfo.getText().toString());
        //Toast.makeText(this, ""+interval.isSelected(), Toast.LENGTH_SHORT).show();

        ar.add(Integer.parseInt(interval.getSelectedItem().toString()));
        ar.add(time_unit.getSelectedItem().toString());

        ar.add(""+NoOfTimes);
        ar.add(null);

        String timersArray[]=new String[NoOfTimes];
        String timersString="";
        for(int i=0;i<NoOfTimes;i++)
        {timersArray[i]=timers[i].getText().toString();timersString+=(timersArray[i]+" ");}

        ar.add(timersString);
        ar.add(starting_time.getText().toString());
        ar.add(starting_date.getText().toString());
        ar.add(ending_date.getText().toString());


        MedReminderObj mro= new MedReminderObj(getApplicationContext(),count, (Integer) ar.get(3),
                NoOfTimes,timersArray,(String)ar.get(4),(String)ar.get(8),
                (String)ar.get(9),(String)ar.get(10),(String)ar.get(1),(String)ar.get(2),(String)ar.get(6));
        mro.setMedReminder();

        Toast.makeText(this,"Reminder added sucessfully", Toast.LENGTH_SHORT).show();
        dh= new DatabaseHandler(this);
        dh.addReminder(ar);

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("frag","Med Reminder");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

