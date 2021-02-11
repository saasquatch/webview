package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.List;

class MyWebViewClient extends WebViewClient {

    protected Context context;

//    MyWebViewClient(Context context) {
//        this.context = context.getApplicationContext();
//    }

//    private boolean findFacebook(Intent intent, View view) {
//        boolean facebookAppFound = false;
//        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
//        for (ResolveInfo info : matches) {
//            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
//                intent.setPackage(info.activityInfo.packageName);
//                facebookAppFound = true;
//                break;
//            }
//        }
//
//        return facebookAppFound;
//    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String hostname;

        // YOUR HOSTNAME
        hostname = "app.referralsaasquatch.com";

        Uri uri = Uri.parse(url);
        if (url.startsWith("file:") || uri.getHost() != null && uri.getHost().endsWith(hostname)) {
            return false;
        }

//        if (url.startsWith("http://ssqt.co/")) {
//            view.loadUrl(url);
//            return true;
//        }
//
//        if (url.startsWith("https://www.facebook.com")) {
//            String shareURL = uri.getQueryParameter("link");
//
//            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
//            facebookIntent.setType("text/plain");
//            facebookIntent.putExtra(Intent.EXTRA_TEXT, shareURL);
//
//            if (findFacebook(facebookIntent, view)) {
//                view.getContext().startActivity(facebookIntent);
//                return true;
//            }
//        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
