package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.R;

import maes.tech.intentanim.CustomIntent;

public class Terms extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    WebView mWebView;
    public final static int LOCATION_INFORMATION = 0;
    public final static int PRIVACY_STATEMENT = 1;
    public final static int TERMS_OF_SERVICE = 2;

    public final static String SET_WEB_VIEW = "set_web_view";

    public TopBar topBar;
    private FragmentManager fm;
    private int BRANCH_WEB_VIEW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        BRANCH_WEB_VIEW = getIntent().getIntExtra(SET_WEB_VIEW, -1);
        mWebView = (WebView) findViewById(R.id.terms_of_use);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
        fm = getSupportFragmentManager();
        topBar = (TopBar)fm.findFragmentById(R.id.top_bar); 
                
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        if(BRANCH_WEB_VIEW == LOCATION_INFORMATION) {
            topBar.setTitleStringWhereUsedEventsAndListStore("위치정보 이용약관");
            mWebView.loadUrl("file:///android_asset/www/location_information.html");
        }
        if(BRANCH_WEB_VIEW == PRIVACY_STATEMENT) {
            topBar.setTitleStringWhereUsedEventsAndListStore("개인정보 취급방칭");
            mWebView.loadUrl("file:///android_asset/www/privacy_statement.html");
        }
        if(BRANCH_WEB_VIEW == TERMS_OF_SERVICE) {
            topBar.setTitleStringWhereUsedEventsAndListStore("이용약관");
            mWebView.loadUrl("file:///android_asset/www/terms_of_service.html");
        }

    }

    @Override
    public void onBack() {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }
}