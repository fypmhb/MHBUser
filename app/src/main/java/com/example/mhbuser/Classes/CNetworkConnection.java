package com.example.mhbuser.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import com.example.mhbuser.R;
import com.google.android.material.snackbar.Snackbar;

public class CNetworkConnection {

    // is connect with internet..
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile == null || !mobile.isConnectedOrConnecting()) && (wifi == null || !wifi.isConnectedOrConnecting());
        } else
            return true;
    }

    // alert dialog
    public AlertDialog.Builder buildDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_cancel_alert);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need a working wifi.\nPress OK to Open wifi Setting\nPress Retry for Retry");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((Activity) context).recreate();


            }
        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myIntent(context);
                //((Activity) context).finish();

            }
        });

        return builder;
    }

    void showSnackBar(View view, final Context context) {
        Snackbar.make(view, "No Internet Connection.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        assert wifiManager != null;
                        if (!wifiManager.isWifiEnabled())
                            wifiManager.setWifiEnabled(true);
                        ((Activity) context).recreate();
                    }
                })
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_orange_light))
                .show();
    }

    private void myIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        ((Activity) context).recreate();

    }
}