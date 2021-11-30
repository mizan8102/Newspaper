package com.mnewspaper.newspaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.util.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;

public class BookViewActivity extends AppCompatActivity {

    private WebView pdfView;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_book_view);
        pdfView=(WebView) findViewById(R.id.pdfView);
        shimmerFrameLayout=(ShimmerFrameLayout)findViewById(R.id.shinnerpdf);
        Intent intent=getIntent();
        String bookurl=intent.getStringExtra("pdfbook");
        //Toasty.info(getApplicationContext(),bookurl,Toasty.LENGTH_SHORT).show();
        pdfView.getSettings().setJavaScriptEnabled(true);
       // pdfView.getSettings().setBuiltInZoomControls(true);
        pdfView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.hideShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);
            }
        });
        String url="";
        try {
            url= URLEncoder.encode(bookurl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +url);

    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.status_color));
            getSupportActionBar().hide();
        }

    }


}