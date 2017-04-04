package com.mohdeva.learn.tasker;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;
import android.os.Handler;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.app.NotificationManager;
import android.app.PendingIntent;


public class MyService extends Service {
    long i = 0;
    static int x = 2; //Seconds
    DBController controller=new DBController(this);
    public static final long INTERVAL= x * 10000;//variable for execute services every x seconds
    private Handler mHandler= new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null; // timer handling
    public Location previousBestLocation = null;


    public MyService() {}
    @Override public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override public void onCreate() {
        super.onCreate();
        // cancel if service is  already existed
        if(mTimer!=null)
            mTimer.cancel();
        else
            mTimer=new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL);// schedule task
    }
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();//cancel the timer
    }
    //Inner Class for Timer
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Runs At Every interval
                    i++;
                    getlocation();
                    if(i==4){

                        //get from db
                        String name="Mohnish";
                        String contact="+917045693777";
                        callNotification(name,contact);
                    }
                    if(i==1){
                        //get from db
                        String name="Mohnish";
                        String contact="+917045693777";
                        String Sms="Hello World";
                        smsNotification(name,contact,Sms);
                    }
                }
            });
        }
    }

    private void callNotification(String Name,String Content) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.todoist_blue)
                        .setContentTitle("Call Reminder")
                        .setContentText("Call "+Name);

//        Intent notificationIntent = new Intent(this, ConfirmCall.class);
        StringBuilder str=new StringBuilder();
        str.append(Name);
        str.append("::");
        str.append(Content);
        String strName=str.toString();
        Intent intent = new Intent(MyService.this, ConfirmCall.class);
        intent.putExtra("Data", strName);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);


        Vibrator V;
        V = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        V.vibrate(100);
        //notification sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(m, builder.build());
    }

    private void smsNotification(String Name,String contact,String SMS) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.todoist_blue)
                        .setContentTitle("SMS Reminder")
                        .setContentText("Send SMS to "+Name);

        StringBuilder str=new StringBuilder();
        str.append(Name);
        str.append("::");
        str.append(contact);
        str.append("::");
        str.append(SMS);
        String strName=str.toString();

        Intent intent2 = new Intent(MyService.this, ConfirmSMS.class);
        intent2.putExtra("Data", strName);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Vibrator V;
        V = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        V.vibrate(100);
        //notification sound
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        manager.notify(m, builder.build());
    }

    public void getlocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,0,locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        Toast.makeText(getApplicationContext(),location.getLatitude()+":"+location.getLongitude(),Toast.LENGTH_SHORT).show();
        Cursor cursor=controller.getalllocationdata();
        String[] result=new String[2];
        float[] temp=new float[1];
        int j;
        if(cursor.moveToFirst()) {
            Toast.makeText(getApplicationContext(),"Andar toh aya Re",Toast.LENGTH_SHORT).show();
            do {
                    result[0] = cursor.getString(0);
                    result[1]=cursor.getString(1);
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(),Double.parseDouble(result[1]),
                            Double.parseDouble(result[0]),temp);
                float dim=temp[0];
                if(dim<100)
                {
                    Intent intent = new Intent(this, Todo.class);
                    // use System.currentTimeMillis() to have a unique ID for the pending intent
                    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                    // build notification
                    // the addAction re-use the same intent to keep the example short
                    Notification n  = new Notification.Builder(this)
                            .setContentTitle("Task Reminder")
                            .setContentText("You have Task Around here")
                            .setSmallIcon(R.drawable.todoist_blue)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true)
                            .addAction(R.drawable.todoist_blue, "And more", pIntent).build();


                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    notificationManager.notify(0, n);
                }
            }while(cursor.moveToNext());
        }
        else {
        }
    }


}