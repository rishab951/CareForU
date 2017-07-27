package com.cbitss.careforu;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 6/28/2017.
 */

public class MedReminderObj  {

    int interval, times;
    String timers[], time_unit;
    String starting_time, starting_date, ending_date, medname, medinfo, weekday;
    AlarmManager alarmMgr;
    PendingIntent alarmIntent;
    Calendar cur_cal;
    Intent intent;
    Context context;
    int alarmReqCode;

    public MedReminderObj(Context context, int count ,int interval, int times, String[] timers, String time_unit, String starting_time,
                          String starting_date,String ending_date, String medname, String medinfo, String weekday) {
        this.context = context;
        this.interval = interval;
        this.times = times;
        this.timers = timers;
        this.time_unit = time_unit;
        this.starting_time = starting_time;
        this.starting_date = starting_date;
        this.ending_date = ending_date;
        this.medname = medname;
        this.medinfo = medinfo;
        this.weekday = weekday;
        alarmReqCode=count;
    }

    public void setMedReminder() {
        int mins = Integer.parseInt(starting_time.substring(starting_time.lastIndexOf(':') + 1));
        int hrs = Integer.parseInt(starting_time.substring(0, starting_time.lastIndexOf(':')));

        cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hrs);
        cal.set(Calendar.MINUTE, mins);

        intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("Reminderid",alarmReqCode);
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (time_unit.equals("hours")) {

            int hr = (int) interval;
            long t=cal.getTimeInMillis();
            long tim= System.currentTimeMillis();
            alarmIntent = PendingIntent.getBroadcast(context,alarmReqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,t, hr * 60 * 60 * 1000, alarmIntent);
        }

        else if (time_unit.equals("days")) {
            PendingIntent pintentDay[]=new PendingIntent[times];

            Map<Integer,Integer[]> map = new HashMap<Integer,Integer[]>();
            for(int i=0;i<times;i++)
                {
                    int hr=Integer.parseInt(timers[i].substring(0,timers[i].indexOf(':')));
                    int min=Integer.parseInt(timers[i].substring(timers[i].indexOf(':')+1));
                    map.put(i,new Integer[]{hr,min});
                }
            for(int i=0;i<times;i++)
            {
                Integer[] ip=map.get(i);
                cal.set(Calendar.HOUR_OF_DAY, ip[0]);
                cal.set(Calendar.MINUTE, ip[1]);
                cal.set(Calendar.SECOND,0);
                pintentDay[i] = PendingIntent.getBroadcast(context, alarmReqCode--, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Log.d("Alarm Times",""+ip[0]+":"+ip[1]+"pp"+cal.getTimeInMillis());

                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintentDay[i]);
            }

        }
        else if (time_unit.equals("weeks")) {
            PendingIntent pintentDay[]=new PendingIntent[times];

            Map<Integer,Integer[]> map = new HashMap<Integer,Integer[]>();
            for(int i=0;i<times;i++)
            {
                int hr=Integer.parseInt(timers[i].substring(0,timers[i].indexOf(':')));
                int min=Integer.parseInt(timers[i].substring(timers[i].indexOf(':')+1));
                map.put(i,new Integer[]{hr,min});
            }
            for(int i=0;i<times;i++)
            {
                Integer[] ip=map.get(i);
                cal.set(Calendar.HOUR_OF_DAY, ip[0]);
                cal.set(Calendar.MINUTE, ip[1]);
                cal.set(Calendar.SECOND,0);

                pintentDay[i] = PendingIntent.getBroadcast(context, alarmReqCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Log.d("Alarm Times",""+ip[0]+":"+ip[1]+"pp"+cal.getTimeInMillis());

                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintentDay[i]);
            }

        }
        else if (time_unit.equals("months")) {
        }
    }


    public void cancelReminder() {
         alarmMgr.cancel(alarmIntent);
    }
}
