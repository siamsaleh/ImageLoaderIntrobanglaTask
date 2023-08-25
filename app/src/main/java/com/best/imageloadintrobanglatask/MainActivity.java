package com.best.imageloadintrobanglatask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Initialize variables
    private ImageView imageView;

    // Initialize BR for checking internet connection
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variables and On click
        imageView = findViewById(R.id.image_view);
        Button loadingBT = findViewById(R.id.btLoad);
        loadingBT.setOnClickListener(this);

        //Checking Internet Connection
        broadcastReceiver = new NetworkChecker();
        regNetworkBroadcast();

        // Here Image is fetched from SharedPreferences
        fetchImage();
    }

    void saveImage(){
        // Convert Bitmap to Base64-encoded string
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Here Saving the encoded image in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ImageData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image_key", encodedImage);
        editor.apply();
    }

    void fetchImage(){
        // Retrieving the encoded image from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ImageData", Context.MODE_PRIVATE);
        String encodedImage = sharedPreferences.getString("image_key", "");

        if (encodedImage.isEmpty()){
            // Zodi sharedpreference e previous image na thake then here this method will load image
            // from the url
            loadRandomImage(imageView);
        }else {
            // Convert the Base64-encoded string to Bitmap
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Displaying the Bitmap in imageview
            imageView.setImageBitmap(decodedBitmap);
        }
    }

    /*This method will load a random image. And this method takes IMAGEVIEW as a parameter in which
    the image will be loaded*/
    private void loadRandomImage(View view){
        String url = "https://picsum.photos/200/300?random=" + randomNumber();
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into((ImageView) view);
    }

    // This method will generate random number
    private int randomNumber(){
        Random number = new Random();
        return number.nextInt(2000);
    }

    // Button OnClick
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btLoad) {
            loadRandomImage(imageView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveImage();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Internet Connection Checking////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("ObsoleteSdkInt")
    protected void regNetworkBroadcast(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void  unregNetwork(){
        try{
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregNetwork();
    }
}