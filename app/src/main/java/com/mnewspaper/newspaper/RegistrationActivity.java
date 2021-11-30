package com.mnewspaper.newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private LinearLayout dateofbirth;
    DatePickerDialog.OnDateSetListener setListener;
    private TextView textView;
    static String data;
    private EditText name,email,address,code;
    private TextInputEditText password,re_password;
    private Button ok;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String remail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_registration);
        dateofbirth=(LinearLayout) findViewById(R.id.txtdate);
        textView=findViewById(R.id.datetext);
        mAuth = FirebaseAuth.getInstance();
        defineValue();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
         remail=email.getText().toString();
        email .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (Patterns.EMAIL_ADDRESS.matcher(remail).matches())
                {
                    //Toasty.makeText(getApplicationContext(),"valid email address",Toasty.LENGTH_SHORT).show();
                    // or
                    //textView.setText("valid email");
                    email.setError(null);
                }
                else
                {
                   // Toasty.error(getApplicationContext(),"Invalid email address",Toasty.LENGTH_SHORT).show();
                   //email.setError("Invalid email address");
                    //or
                    //textView.setText("invalid email");
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        Calendar calendar=Calendar.getInstance();
        final  int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(RegistrationActivity.this
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                 data=i+"/"+i1+"/"+i2;
                textView.setText(data);
            }
        };
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                ok.setVisibility(View.GONE);
                regist();
            }
        });
        

    }

    private void regist() {

        String rname=name.getText().toString();
        remail=email.getText().toString();
        String rpass=password.getText().toString();
        String repass=re_password.getText().toString();
        String codeid=code.getText().toString();
        String addressid=address.getText().toString();
        if (TextUtils.isEmpty(rname)){
            name.setError("required*");
            Toasty.error(RegistrationActivity.this,"name is empty",Toasty.LENGTH_SHORT).show();
            name.requestFocus();
        }else if (TextUtils.isEmpty(remail)){
            email.setError("required*");
            Toasty.error(RegistrationActivity.this,"email is empty",Toasty.LENGTH_SHORT).show();
            email.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(remail).matches()){
            email.setError("Invalid email!");
            Toasty.error(RegistrationActivity.this,"email is empty",Toasty.LENGTH_SHORT).show();
            email.requestFocus();
        }
        else if (TextUtils.isEmpty(rpass)){
            password.setError("enter password!");
            password.requestFocus();
            Toasty.error(RegistrationActivity.this,"password field is empty",Toasty.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(repass)){
            password.setError("enter password!");
            password.requestFocus();
            Toasty.error(RegistrationActivity.this,"password field is empty",Toasty.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressid)){
            password.setError("enter password!");
            password.requestFocus();
            Toasty.error(RegistrationActivity.this,"enter your address",Toasty.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(codeid)){
            code.setError("enter code!");
            code.requestFocus();
            Toasty.error(RegistrationActivity.this,"enter code",Toasty.LENGTH_SHORT).show();
        }
        else if (! rpass.contains(repass)){
            re_password.setError("re-password isn't matched!");
            re_password.requestFocus();
            Toasty.error(RegistrationActivity.this,"re-password isnot matched",Toasty.LENGTH_SHORT).show();
        }
        else if (!"01645316379".contains(codeid)){
            code.setError("enter password!");
            password.requestFocus();
            Toasty.error(RegistrationActivity.this,"Code isn't match!Contact Developer",Toasty.LENGTH_LONG).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(remail, rpass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid().toString()).child("personal_info");
                                            HashMap<Object,String > hashMap=new HashMap<>();
                                            hashMap.put("name",rname);
                                            hashMap.put("birth",data);
                                            hashMap.put("address",addressid);
                                          databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if (task.isSuccessful()){
                                                      startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                                                      Animatoo.animateFade(RegistrationActivity.this);
                                                      finish();
                                                      Toasty.info(getApplicationContext(),"Check your email and Verify your Account",Toasty.LENGTH_LONG).show();
                                                  }
                                              }
                                          });



                                        }
                                        else {
                                            Toasty.info(getApplicationContext(),"Sorry!Try try again",Toasty.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            ok.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                ok.setVisibility(View.VISIBLE);
                                Toasty.error(getApplicationContext(),"Registration Failed!Try again",Toasty.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });
        }

    }

    private void defineValue() {
        name=findViewById(R.id.txtname);
        email=findViewById(R.id.txtemail);
        address=findViewById(R.id.txtaddress);
        password=findViewById(R.id.txtpassword);
        re_password=findViewById(R.id.txtrepassword);
        code=findViewById(R.id.txtcode);
        ok=findViewById(R.id.registrationbtn);
        progressBar=findViewById(R.id.registrationprogress);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.home_background));
            getSupportActionBar().hide();
        }

    }
}