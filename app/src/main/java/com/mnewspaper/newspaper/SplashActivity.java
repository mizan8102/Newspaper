package com.mnewspaper.newspaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeStatusBarColor();

        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ValuerText();
            }
        });
        thread.start();

    }
    void ValuerText() {
        for (int i = 50; i != 100; ) {
            i = i + 50;
            try {
                Thread.sleep(1000);
                //progressBar.setProgress(i);


                if (i == 100) {
                    SharedPreferences sp=getApplicationContext().getSharedPreferences
                            ("com.mnewspaper.newspaper", Context.MODE_PRIVATE);
                    String email=sp.getString("name","");
                    if (TextUtils.isEmpty(email)){
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateFade(this);
                    }
                    else {
                        Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateFade(SplashActivity.this);
                    }


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
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