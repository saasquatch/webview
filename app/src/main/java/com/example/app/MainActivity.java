package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.saasquatch.android.SquatchJavascriptInterface;
import com.saasquatch.sdk.ClientOptions;
import com.saasquatch.sdk.SaaSquatchClient;
import com.saasquatch.sdk.input.RenderWidgetInput;
import com.saasquatch.sdk.input.UserIdInput;
import com.saasquatch.sdk.input.WidgetTypes;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends Activity {

    private WebView mWebView;
    private SaaSquatchClient saasquatchClient;

//    @Override
//    public Context getApplicationContext() {
//        return super.getApplicationContext();
//    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saasquatchClient = SaaSquatchClient.create(ClientOptions.newBuilder()
                .setAppDomain("staging.referralsaasquatch.com")
                .setTenantAlias("test_aisnwipcdkk5k")
                .build());

        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
//        mWebView.setWebViewClient(new MyWebViewClient(getBaseContext()));
//        mWebView.addJavascriptInterface(new WebAppInterface(this), "SquatchAndroid");
        SquatchJavascriptInterface.applyToWebView(mWebView);

        Single.fromPublisher(saasquatchClient.renderWidget(RenderWidgetInput.newBuilder()
//                .setUser(UserIdInput.of("accId", "userId"))
                .setWidgetType(WidgetTypes.ofProgramWidget("new-schema-test", "referredWidget"))
                .build(), null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(apiResponse -> {
//                    final String widgetTemplate = apiResponse.getData();
//                    // do whatever you want with it
//                    System.out.println(widgetTemplate);
                    String widgetTemplate = "<html> <body> <sqh-global-container background=\"#ffffff\" loadingcolor=\"#439B76\" fontfamily=\"Helvetica Neue,Helvetica,Arial,sans-serif\" poweredby=\"true\"><sqh-image-component ishidden=\"false\" url=\"https://d2rcp9ak152ke1.cloudfront.net/theme/test_azu3qtbbzj0ta/assets/WkKexbBO/images/conversion.png\" alignment=\"center\" width=\"140\" css=\"padding-top: 38px; padding-bottom: 7px;\"></sqh-image-component><sqh-text-component sqhheader=\"true\" ishidden=\"false\" ismarkdown=\"false\" color=\"#000\" fontsize=\"24\" text=\"You were Successfully Referred to Saasquatch\" textalign=\"center\" paddingtop=\"12\" paddingbottom=\"20\"></sqh-text-component><sqh-text-component sqhbody=\"true\" ishidden=\"false\" ismarkdown=\"false\" color=\"gray\" rewardkey=\"referredReward\" fontsize=\"13\" text=\"<hr style='border-top: 1px solid #eee; border-bottom: 0;'></hr><br/><br/>Use the Referral Code at checkout to receive your referral reward\" ismarkdown=\"true\" textalign=\"center\" paddingtop=\"10\" paddingbottom=\"20\"></sqh-text-component><sqh-copy-button ishidden=\"false\" copysuccess=\"copied!\" copyfailure=\"Press Ctrl+C to copy\" codefontcolor=\"#000000\" codefontsize=\"14\" text=\"COPY CODE\" fontsize=\"14\" width=\"170\" backgroundcolor=\"#35b21e\" textcolor=\"#ffffff\" borderradius=\"4\" rewardkey=\"referredReward\" codefontsize=\"14\" codefontcolor=\"#000000\"></sqh-copy-button></sqh-global-container> </body> </html>";
//                    String widgetTemplate = "<h1>hi</h1>";
                    String encodedHtml = Base64.encodeToString(widgetTemplate.getBytes(), Base64.NO_PADDING);
                    mWebView.loadData(encodedHtml, "text/html", "base64");
//                    mWebView.loadData(widgetTemplate, "text/html", "UTF-8");
                })
                .subscribe();

        // REMOTE RESOURCE
//         mWebView.loadUrl("https://app.referralsaasquatch.com/a/test_aw8cmqwv4sy58/widgets/mobilewidget?userId=noahtest&firstName=noah&lastName=testerson&accountId=noahtest&paymentProviderId=NULL&email=noah%40example.com&jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoibm9haHRlc3QiLCJhY2NvdW50SWQiOiJub2FodGVzdCJ9fQ._7dOoIm5YPddKm8zkKGo2XH0J-DQhvowAthvLESNctc");
//         mWebView.loadData(data, "text/html", "UTF-8");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saasquatchClient.close();
    }

    @Override
    public void onBackPressed() {
//        if(mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
        SquatchJavascriptInterface myInterface = SquatchJavascriptInterface.create(getApplicationContext());
        myInterface.shareOnFacebook("www.google.com", "http://www.google.com");
    }
}
