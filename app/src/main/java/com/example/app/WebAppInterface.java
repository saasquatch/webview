package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

public class WebAppInterface {
    Context mContext;

    /**
     * Instantiate the interface and set the context
     */
    WebAppInterface(Context c) {
        mContext = c;
    }

    public static void applyToActivity(WebView myActivity){
        myActivity.addJavascriptInterface(new WebAppInterface(myActivity.getContext()), "SquatchAndroid");
    }

    /**
     * Share on Facebook with browser fallback
     */
    @JavascriptInterface
    public void shareOnFB(String shareURL, String fallbackURL) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareURL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // See if official Facebook app is found
        // From https://stackoverflow.com/questions/7545254/android-and-facebook-share-intent
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        // As fallback to a browser
        if (facebookAppFound) {
            mContext.startActivity(intent);
        } else {
            Intent fallbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackURL));
            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(fallbackIntent);
        }
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}