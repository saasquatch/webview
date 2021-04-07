package com.example.app;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.saasquatch.android.SquatchAndroid;
import com.saasquatch.android.SquatchJavascriptInterface;
import com.saasquatch.android.input.AndroidRenderWidgetOptions;
import com.saasquatch.sdk.ClientOptions;
import com.saasquatch.sdk.RequestOptions;
import com.saasquatch.sdk.SaaSquatchClient;
import com.saasquatch.sdk.auth.AuthMethod;
import com.saasquatch.sdk.input.RenderWidgetInput;
import com.saasquatch.sdk.input.UserIdInput;
import com.saasquatch.sdk.input.WidgetType;
import com.saasquatch.sdk.internal.json.GsonUtils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

  static final String tenantAlias = "test_anflbjnde3o91",
  accountId = "GRMOA4D5UHT482FF", userId = "5ef39052e4b0aa52a7b1a852",
  programId = "r1";

  private WebView mWebView;
  private SaaSquatchClient saasquatchClient;
  private SquatchAndroid squatchAndroid;

  @Override
  @SuppressLint("SetJavaScriptEnabled")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    saasquatchClient = SaaSquatchClient.create(ClientOptions.newBuilder()
        .setAppDomain("staging.referralsaasquatch.com")
        .setTenantAlias(tenantAlias)
        .build());
    this.squatchAndroid = SquatchAndroid.create(saasquatchClient);

    setContentView(R.layout.activity_main);
    mWebView = findViewById(R.id.activity_main_webview);
    WebSettings webSettings = mWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setAllowContentAccess(true);
    webSettings.setAllowFileAccess(true);
    mWebView.setWebChromeClient(new WebChromeClient());
//    mWebView.setWebViewClient(new MyWebViewClient());
    SquatchJavascriptInterface.applyToWebView(mWebView);
//
//    Single.fromPublisher(saasquatchClient.renderWidget(RenderWidgetInput.newBuilder()
//        .setUser(UserIdInput.of("1234", "1234"))
//        .setEngagementMedium("UNKNOWN")
//        .setWidgetType(WidgetType.ofProgramWidget("new-schema-test", "referrerWidget"))
//        .build(), RequestOptions.newBuilder()
//        .setAuthMethod(AuthMethod.ofTenantApiKey("TEST_1GQihVWTUfncgmzA8Hb6Nike07rZrQke"))
//        .build()))
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .doOnSuccess(apiResponse -> {
//          final String widgetTemplate = apiResponse.getData();
//          String encodedHtml = Base64.encodeToString(widgetTemplate.getBytes(UTF_8), Base64.DEFAULT);
//          mWebView.loadData(encodedHtml, "text/html", "base64");
////          mWebView.loadDataWithBaseURL("https://staging.referralsaasquatch.com", widgetTemplate, "text/html", null, null);
//        })
//        .subscribe();

    Single.fromPublisher(squatchAndroid.renderWidget(RenderWidgetInput.newBuilder()
        .setUser(UserIdInput.of(accountId, userId))
        .setEngagementMedium("UNKNOWN")
        .setWidgetType(WidgetType.ofProgramWidget(programId, "referrerWidget"))
        .build(), RequestOptions.newBuilder()
        .setAuthMethod(AuthMethod.ofTenantApiKey("TEST_j5F7WMc3QvZiomD0bo4eXtlePQaBOlVa"))
        .build(), AndroidRenderWidgetOptions.ofWebView(mWebView)))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(apiResponse -> {
          final String widgetTemplate = apiResponse.getData();
          String encodedHtml = Base64.encodeToString(widgetTemplate.getBytes(UTF_8), Base64.DEFAULT);
          mWebView.loadData(encodedHtml, "text/html", "base64");
        })
        .toFlowable()
        .onErrorResumeNext(t -> {
          t.printStackTrace();
          return Flowable.empty();
        })
        .subscribe();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      saasquatchClient.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onBackPressed() {
    SquatchJavascriptInterface myInterface =
//        SquatchJavascriptInterface
//        .create(getApplicationContext());
    SquatchJavascriptInterface.newBuilder()
        .setSquatchAndroid(squatchAndroid)
        .setApplicationContext(getApplicationContext())
        .build();
    final Map<String, Object> userMap = new HashMap<>();
    userMap.put("accountId", accountId);
    userMap.put("id", userId);
    final Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("user", userMap);
    paramMap.put("shareLink", "https://example.com");
    paramMap.put("messageLink", "https://google.com");
    paramMap.put("tenantAlias", tenantAlias);
    paramMap.put("programId", programId);
    myInterface.shareOnFacebook(GsonUtils.gson.toJson(paramMap));
//    myInterface.shareOnFacebook("www.google.com", "http://www.google.com");
  }
}
