package com.mohdeva.learn.tasker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent myIntent = new Intent(context, MyService.class);
        context.startService(myIntent);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
