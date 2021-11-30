package com.mnewspaper.newspaper.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mnewspaper.newspaper.R;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class BookUploadActivity extends AppCompatActivity {

    private LinearLayout chooseFile;
    private EditText bookname;
    private ImageView imageView;
    private Button savebutton;
    private int PICK_PDF_CODE=10;
    TextView filename,percenttxt;
    private ProgressBar progressBar;
    StorageReference storageReference;
     BottomSheetDialog sheetDialog;
     private FirebaseAuth firebaseAuth;
     private DatabaseReference databaseReference;
     private ImageView imageViewbook;
    Uri pathfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_book_upload);
        chooseFile=(LinearLayout)findViewById(R.id.chooseFile);
        firebaseAuth=FirebaseAuth.getInstance();


        storageReference= FirebaseStorage.getInstance().getReference().child("pdf");
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 sheetDialog=new BottomSheetDialog(
                        BookUploadActivity.this,R.style.BottomSheetDialogTheme);

                View bottonView= LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.layout_bottom_sheet,(LinearLayout)findViewById(R.id.bottomSheetCotainer));
                sheetDialog.setContentView(bottonView);
                sheetDialog.show();
                bookname=sheetDialog.findViewById(R.id.booknametxt);
                imageView=sheetDialog.findViewById(R.id.pdfselect);
                filename=sheetDialog.findViewById(R.id.filename);
                progressBar=sheetDialog.findViewById(R.id.progress_bar_sheet);
                percenttxt=sheetDialog.findViewById(R.id.uploadingtxt);
                imageViewbook=findViewById(R.id.close_book);
                imageViewbook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                sheetDialog.findViewById(R.id.pdfsave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        savefile();
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //creating an intent for file chooser
                       getPDF();
                    }
                });
            }

        });


    }
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    private void savefile() {
        String book=bookname.getText().toString();
        if ( TextUtils.isEmpty(book)){
            Toasty.error(getApplicationContext(),"book name missing..!",Toasty.LENGTH_SHORT).show();
        }
        else if (pathfile !=null) {
            progressBar.setVisibility(View.VISIBLE);
            percenttxt.setVisibility(View.VISIBLE);
            StorageReference sRef = storageReference.child(new Random().nextInt()+System.currentTimeMillis() + ".pdf");
            sRef.putFile(pathfile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // progressBar.setVisibility(View.GONE);
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference= FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid().toString()).child("books").child(UUID.randomUUID().toString());

                                    HashMap<Object,String > hashMap=new HashMap<>();
                                    hashMap.put("bookname",book);
                                    hashMap.put("pdf",uri.toString()+".pdf");
                                    databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toasty.info(getApplicationContext(), "File Uploaded Successfully", Toasty.LENGTH_SHORT).show();
                                            sheetDialog.dismiss();
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                            //Upload upload = new Upload(editTextFilename.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                            //mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toasty.error(getApplicationContext(), exception.getMessage(), Toasty.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //int a=

//                        progressDialog.setMessage();
                            progressBar.setProgress((int) progress);
                            percenttxt.setText("Uploaded " + (int) progress + "%");
                        }
                    });
        }else {
            Toasty.error(getApplicationContext(),"file couldn't find!",Toasty.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                //uploadFile(data.getData());
                pathfile=data.getData();
                filename.setText("file has selected!");

            }else{
                Toasty.error(getApplicationContext(),"file cann't find",Toasty.LENGTH_SHORT).show();
            }
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}