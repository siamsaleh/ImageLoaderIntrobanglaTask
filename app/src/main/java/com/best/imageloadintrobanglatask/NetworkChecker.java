package com.best.imageloadintrobanglatask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NetworkChecker extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Global Context for Dialog
        this.context = context;


        if (isConnected(context)){
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
        }else {
            showDialog();
        }
    }

    //
    public boolean isConnected(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public void showDialog(){
        // If the Network connection is not ok. Then this Dialog will popup
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_layout, null);
        Button okBT = view.findViewById(R.id.ok_button);
        builder.setView(view);

        final Dialog dialog = builder.create();
        okBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
