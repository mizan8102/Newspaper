package com.mnewspaper.newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class LinkUpload extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText linknamet,linkt;
    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_link_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Link");
        firebaseAuth=FirebaseAuth.getInstance();
        String ui=firebaseAuth.getCurrentUser().getUid();
        linknamet=findViewById(R.id.linkname);
        linkt=findViewById(R.id.link);
        Button button=findViewById(R.id.uploadlink);
        lottieAnimationView=findViewById(R.id.lottieload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String linkname=linknamet.getText().toString();
                String link =linkt.getText().toString();
                if (TextUtils.isEmpty(linkname)){
                    linknamet.setError(R.drawable.applogo+" Error");
                }
                else if (TextUtils.isEmpty(link)){
                    linkt.setError(R.drawable.applogo+" Error");
                }else {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(ui).child("link").child(UUID.randomUUID().toString());
                    HashMap<Object,String> hashMap=new HashMap<>();
                    hashMap.put("linkname",linkname);
                    hashMap.put("link",link);
                    databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.info(getApplicationContext(),"Uploaded successful..",Toasty.LENGTH_SHORT).show();
                            linknamet.setText("");
                            linkt.setText("");
                            lottieAnimationView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}