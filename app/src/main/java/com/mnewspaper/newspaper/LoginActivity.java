package com.mnewspaper.newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    View daySky,nightSky;
    ImageView dayLand,nightLand,sun;
    DayNightSwitch dayNightSwitch;
    Button login_btn;
    ProgressBar progressBar;
    private EditText email,password;
     DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private TextView textView;
    static private String name;
   static private String address;
   static private String birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        sun=(ImageView) findViewById(R.id.sun);
        dayLand=(ImageView) findViewById(R.id.day_landscape);
        nightLand=(ImageView) findViewById(R.id.night_landscape);
        daySky=(View) findViewById(R.id.day_bg);
        nightSky=(View)findViewById(R.id.night_bg);
        dayNightSwitch=(DayNightSwitch)findViewById(R.id.day_night_switch);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        login_btn=findViewById(R.id.login_btn);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        textView=findViewById(R.id.tocreateaccount);
        databaseReference=FirebaseDatabase.getInstance().getReference();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                Animatoo.animateFade(LoginActivity.this);
                finish();
            }
        });
        sun.setTranslationY(-110);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night){
                    sun.animate().translationY(110).setDuration(1000);
                    dayLand.animate().alpha(0).setDuration(1300);
                    daySky.animate().alpha(0).setDuration(1300);

                }else {
                    sun.animate().translationY(-110).setDuration(1000);
                    dayLand.animate().alpha(1).setDuration(1300);
                    daySky.animate().alpha(1).setDuration(1300);
                }

            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                login_btn.setVisibility(View.GONE);
                if (!isConnected()){
                    showCustomDialogue();
                }else {
                    loginData();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }

    }



    public void login_btn(View view) {



    }

    private void loginData() {
        String emaile=email.getText().toString();
        String passworde=password.getText().toString();
        if (TextUtils.isEmpty(emaile)){
            Toasty.error(getApplicationContext(),"email address is missing !").show();

        }
        else if (TextUtils.isEmpty(passworde)){
            Toasty.error(getApplicationContext(),"password is missing !").show();
        }
        else {

            mAuth.signInWithEmailAndPassword(emaile, passworde)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                if (mAuth.getCurrentUser().isEmailVerified()){
                                    final String[] namesh = new String[1];
                                    final String[] addressh = {""};
                                    final String[] birthdaysh = {""};
                                    String ui=mAuth.getCurrentUser().getUid();
                                    DatabaseReference dref=databaseReference.child(ui).child("personal_info");
                                    dref.addValueEventListener(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                namesh[0] =snapshot.child("name").getValue(String.class);
                                                addressh[0] =snapshot.child("address").getValue(String.class);
                                                birthdaysh[0]=snapshot.child("birth").getValue(String.class);
                                            SharedPreferences sp = getApplicationContext().getSharedPreferences
                                                    ("com.mnewspaper.newspaper", Context.MODE_PRIVATE);
                                            sp.edit().putString("name", namesh[0]).apply();
                                            sp.edit().putString("address", addressh[0]).apply();
                                            sp.edit().putString("birthday", birthdaysh[0]).apply();
                                          //  sp.edit().putString("imageUrl", image[0]).apply();
                                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                            Animatoo.animateFade(LoginActivity.this);
                                            finish();

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }


                            } else {
                                // If sign in fails, display a message to the user.
                                progressBar.setVisibility(View.INVISIBLE);
                                login_btn.setVisibility(View.VISIBLE);
                                Toasty.error(getApplicationContext(),"Sorry !Login failed ",Toasty.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }


    // Check Internet Connection

    private void showCustomDialogue() {
      AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
      builder.setMessage("Please connect to the internet to proceed further")
              .setCancelable(false)
              .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                      finish();

                  }
              })
              .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                  }
              }).show();
           // return true;

    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();



    }
}