package com.mnewspaper.newspaper.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mnewspaper.newspaper.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class Blog_UploadActivity extends AppCompatActivity {

    private ImageView imagebutton,imageView;
    private Button button;
    private EditText title,description;
    private int PICK_IMAGE =20;
    private Uri urii=null;
    FirebaseAuth firebaseAuth;
    private String uid=null;
    private LottieAnimationView lottieAnimationView;
    private StorageReference reference;
    private ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_blog__upload);
        // Variable inialiazation
        imagebutton=(ImageView)findViewById(R.id.seletctPicture);
        imageView=(ImageView)findViewById(R.id.blogPic);
        firebaseAuth=FirebaseAuth.getInstance();
        uid=firebaseAuth.getCurrentUser().getUid().toString();
        title=(EditText)findViewById(R.id.titletext);
        description=(EditText)findViewById(R.id.textdecription);


     reference=FirebaseStorage.getInstance().getReference().child(uid).child("blog").child(UUID.randomUUID().toString());

        button=(Button)findViewById(R.id.txtsubmit);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectIntent=new Intent();
                selectIntent.setType("image/*");
                selectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(selectIntent,PICK_IMAGE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urii!=null) {
                    String titlet = title.getText().toString();
                    String des = description.getText().toString();
                    if (TextUtils.isEmpty(titlet)) {
                        title.setError("enter title");

                    } else if (TextUtils.isEmpty(des)) {

                        description.setError("enter description");
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                                Locale.ENGLISH);
                        String currentDate=simpleDateFormat.format(new Date());
                        Date currentTime = Calendar.getInstance().getTime();
                        progressDialog = new ProgressDialog(Blog_UploadActivity.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        reference.putFile(urii).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String mm=UUID.randomUUID().toString();
                                        databaseReference=FirebaseDatabase.getInstance().getReference().child(uid).child("blog").child(mm);

                                        HashMap<Object,String> hashMap=new HashMap<>();
                                        hashMap.put("title",titlet);
                                        hashMap.put("description",des);
                                        hashMap.put("image",uri.toString());
                                        hashMap.put("date",currentDate);
                                        hashMap.put("key",mm);
                                        databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toasty.info(getApplicationContext(), "upload Sucessful", Toasty.LENGTH_LONG).show();
                                                title.setText("");
                                                description.setText("");
                                                urii=null;
                                                imageView.setImageBitmap(null);
                                                imageView.setVisibility(View.GONE);

                                                progressDialog.dismiss();
                                            }
                                        });





                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(getApplicationContext(), "Sorry!file cann't uploaded", Toasty.LENGTH_SHORT).show();

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
                    }
                }
                else {
                    Toasty.error(getApplicationContext(),"file cann't found",Toasty.LENGTH_SHORT).show();
                }
            }
            });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        urii=data.getData();
        if (requestCode==PICK_IMAGE && urii!=null){
            imageView.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),urii);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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