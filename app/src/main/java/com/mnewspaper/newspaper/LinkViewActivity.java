package com.mnewspaper.newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnewspaper.newspaper.Adapter.LinkAdapter;
import com.mnewspaper.newspaper.Model.LinkModel;

import java.util.ArrayList;
import java.util.List;

public class LinkViewActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
   // private LinkModel linkModel;
    private List<LinkModel> linkModels;
    private EditText searchtext;
    private  LinkAdapter linkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_link_view);
        recyclerView=(RecyclerView)findViewById(R.id.linkview);

        firebaseAuth=FirebaseAuth.getInstance();
                String id =firebaseAuth.getCurrentUser().getUid().toString();

        databaseReference= FirebaseDatabase.getInstance().getReference().child(id).child("link");
        searchtext=(EditText)findViewById(R.id.searchtext);
        linkModels=new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkModels.clear();
                    for (DataSnapshot sp:snapshot.getChildren()){
                        //Toast.makeText(getApplicationContext(),snapshot.toString(),Toast.LENGTH_SHORT).show();
                       LinkModel linkModel=new LinkModel();
                        linkModel.setLink(sp.child("link").getValue(String.class));
                        linkModel.setLinkname(sp.child("linkname").getValue(String.class));
                        linkModels.add(linkModel);
                    }
                linkAdapter=new LinkAdapter(LinkViewActivity.this,linkModels);
                recyclerView.setLayoutManager(new LinearLayoutManager(LinkViewActivity.this,LinearLayoutManager.VERTICAL,false));
                recyclerView.setAdapter(linkAdapter);
                linkAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userInput=charSequence.toString().toLowerCase();
                if (TextUtils.isEmpty(userInput)){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            linkModels.clear();
                            for (DataSnapshot sp:snapshot.getChildren()){
                                //Toast.makeText(getApplicationContext(),snapshot.toString(),Toast.LENGTH_SHORT).show();
                                LinkModel linkModel=new LinkModel();
                                linkModel.setLink(sp.child("link").getValue(String.class));
                                linkModel.setLinkname(sp.child("linkname").getValue(String.class));
                                linkModels.add(linkModel);
                            }
//                            linkAdapter=new LinkAdapter(LinkViewActivity.this,linkModels);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(LinkViewActivity.this,LinearLayoutManager.VERTICAL,false));
//                            recyclerView.setAdapter(linkAdapter);
                            linkAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                ArrayList<LinkModel> arrayList=new ArrayList<>();
                for (LinkModel linkm:linkModels){
                    if (linkm.getLinkname().toLowerCase().contains(userInput)){

                        arrayList.add(linkm);

                    }
                }
                linkModels.clear();
                linkModels.addAll(arrayList);
                linkAdapter.notifyDataSetChanged();



            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });




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