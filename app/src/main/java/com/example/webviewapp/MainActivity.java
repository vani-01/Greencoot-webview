package com.example.webviewapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URISyntaxException;


public class MainActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;

    private static final String TAG = "MainActivity";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });


        /**
         *      Action to trigger dialing phone number, sending email, opening social media pages in Webview app...
         */

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                }else if(url.startsWith("mailto:")){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(intent);
                }else if(url.startsWith("https://www.facebook.com")){
                    String facebookId = "https://www.facebook.com/profile.php?id=100581859132043";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId));
                    startActivity(intent);
                    Log.d(TAG, "shouldOverrideUrlLoading: " + intent);
                }else if(url.startsWith("https://www.instagram.com")){
                        startActivity(instaIntn(getApplicationContext()));
                }else if(url.startsWith("twitter:")){
                        //startActivity(twitterIntn(getApplicationContext()));
                        startActivity(startTwitter(getApplicationContext()));
                }else if(url.startsWith("upi")){                                                       //https://www.instamojo.com/@greencoot/a001bc6b754d48f491d1aec1a7acc9cc
                    Intent intent = new Intent(Intent.ACTION_VIEW);  // To show app chooser
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    Log.d(TAG, "shouldOverrideUrlLoading: " + intent);
                }else if(url.startsWith("intent")){
                         //need to open upi payment apps when clicking specific app icon ..but now it just shows expiry time screen...

                }else{
                    view.loadUrl(url);
                }
               return true;
            }

        });

        webView.loadUrl("http://greencoot.com");


    }

    // function to open instagram app  ....
    private Intent instaIntn(Context context) {
        Intent i1;
        String instaId = "green_coot";
        String appResolver = "http://instagram.com/_u/";
        String webResolver = "https://instagram.com/";

        String instaPackageName = "com.instagram.android";
        String instaLitePackName = "com.instagram.lite";

        try {
            context.getPackageManager().getPackageInfo(instaPackageName, 0);
            i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+instaId));
        } catch (PackageManager.NameNotFoundException e1) {
            try {
                context.getPackageManager().getPackageInfo(instaLitePackName, 0);
                i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+instaId));
            } catch (PackageManager.NameNotFoundException e2) {
                i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(webResolver+instaId));
            }
        }
        return i1;
    }

    /*private Intent twitterIntn(Context context) {
        Intent i1;
        String twitterId = "greencoot";
        String appResolver = "twitter://user?screen_name=";
        String webResolver = "https://twitter.com/";
        String twitterPackageName = "com.twitter.android";
        String twitterLitePackName = "com.twitter.lite";

        try {
            context.getPackageManager().getPackageInfo(twitterPackageName, 0);
            i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+twitterId));
        } catch (PackageManager.NameNotFoundException e1) {
            Toast.makeText(getApplication(), "twitter not found", Toast.LENGTH_SHORT).show();
            try {
                context.getPackageManager().getPackageInfo(twitterLitePackName, 0);
                i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(appResolver+twitterId));
            } catch (PackageManager.NameNotFoundException e2) {
                Toast.makeText(getApplication(), "twitter and twitter lite not found", Toast.LENGTH_SHORT).show();
                i1 = new Intent(Intent.ACTION_VIEW, Uri.parse(webResolver+twitterId));
            }
        }
        return i1;
    }*/

    private Intent startTwitter(Context context) {

        Intent intent = null;
        try {
            // get the Twitter app if possible
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=greencoot"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/greencoot"));
        }
        startActivity(intent);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}


