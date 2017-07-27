package com.cbitss.careforu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Harpreet on 12/23/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
        static Ringtone ringtone;
        String ending_date;
        @Override
        public void onReceive(Context context, Intent intent) {

            int a=intent.getIntExtra("Reminderid",2332);
            Log.d("integeralarmReceiver ",""+a);

            DatabaseHandler dh= new DatabaseHandler(context);
            ArrayList all=dh.getReminder(a);
            ending_date= (String) all.get(10);
            if( checkEndingDate()==0)
            {
                Toast.makeText(context, "received", Toast.LENGTH_SHORT).show();
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (alarmUri == null) {
                    alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                }

                //this will update the UI with message
                ringtone = RingtoneManager.getRingtone(context, alarmUri);
                ringtone.play();

                Intent i = new Intent().setClass(context,AlarmActivity.class);
                i.putExtra("AlarmActivityid",a);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(i);
            }
            else
            {

                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                int times=Integer.parseInt((String) all.get(4));
                if(times>1)
                {
                    for(int i=0;i<times;i++)
                    {
                        PendingIntent pi = PendingIntent.getBroadcast(context,a--, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmMgr.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+  60 * 1000, pi);
                        alarmMgr.cancel(pi);
                    }
                }
                else
                {
                    PendingIntent pi = PendingIntent.getBroadcast(context,a, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+  60 * 1000, pi);
                    alarmMgr.cancel(pi);
                }


            }

      }

    public int checkEndingDate()
    {
         String temp = ending_date.substring(0, ending_date.lastIndexOf('/'));
        int year = Integer.parseInt(ending_date.substring(ending_date.lastIndexOf('/') + 1));
        int day = Integer.parseInt(temp.substring(0, temp.lastIndexOf('/')));
        int month = Integer.parseInt(temp.substring(temp.lastIndexOf('/') + 1));

        Calendar cur_date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(cur_date.getTime());
        temp = formattedDate.substring(0, formattedDate.lastIndexOf('/'));
        int cur_year = Integer.parseInt(formattedDate.substring(formattedDate.lastIndexOf('/') + 1));
        int cur_day = Integer.parseInt(temp.substring(0, temp.lastIndexOf('/')));
        int cur_month = Integer.parseInt(temp.substring(temp.lastIndexOf('/') + 1));

        if (cur_year == year) {

            if (cur_month == month+1)
            {
                if(month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
                    if (cur_day == 1 && day == 31)
                      return 1;
                }
                else if(month==2||month==4||month==6||month==9||month==11)
                {
                    if (cur_day == 1 && day == 30)
                     return 1;
                }
            }

            else if (cur_month == month)
                if (cur_day == day + 1)
                    return 1;
        }
        return 0;
    }
}
