package com.mnewspaper.newspaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnewspaper.newspaper.Adapter.BlogAdapter;
import com.mnewspaper.newspaper.Adapter.BookViewAdapter;
import com.mnewspaper.newspaper.Adapter.CatagoryAdapter;
import com.mnewspaper.newspaper.Model.BlogModel;
import com.mnewspaper.newspaper.Model.CatagoryModel;
import com.mnewspaper.newspaper.Model.InfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView, newrecycler;
    private List<CatagoryModel> list;
    private DatabaseReference databaseReference;
    private CatagoryAdapter catagoryAdapter;
    private String api = "";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView menuIcon;
    static final float END_SCALE=0.7f;
    private RelativeLayout contentView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ScrollView forshiner;
    private ImageView adddashboard;
    private FirebaseAuth firebaseAuth;
    private List<InfoModel> booklist;
    private List<BlogModel>bloglist;
    private BlogAdapter blogAdapter;

    private LinearLayout linkpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setContentView(R.layout.activity_home);
        list = new ArrayList<>();
        booklist=new ArrayList<>();
        bloglist=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewCatagory);
        newrecycler = (RecyclerView) findViewById(R.id.popularnews);
        //contentView=(LinearLayout)findViewById(R.id.content);
        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        shimmerFrameLayout=(ShimmerFrameLayout)findViewById(R.id.shinner);
        menuIcon = findViewById(R.id.menu_icon);
        forshiner=(ScrollView)findViewById(R.id.forshiner);
        contentView = findViewById(R.id.content);
        firebaseAuth=FirebaseAuth.getInstance();
        adddashboard=(ImageView)findViewById(R.id.adddashboard);
        adddashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,DashboardRetailar_Activity.class));
                Animatoo.animateFade(HomeActivity.this);
            }
        });
        linkpage=findViewById(R.id.linkpage);
        linkpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,LinkViewActivity.class));
                Animatoo.animateFade(HomeActivity.this);
            }
        });
        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.hideShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    forshiner.setVisibility(View.VISIBLE);
            }
        },5000);
        //Navigation


        navigationDrawer();


        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid().toString());
       // fetch();
        pdfView();
        //newsarticle();

        blogview();

    }

    private void blogview() {
        DatabaseReference drefer=databaseReference.child("blog");
        drefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bloglist.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    BlogModel blogModel=new BlogModel();
                    blogModel.setBdes(ds.child("description").getValue(String.class));
                    blogModel.setBtitle(ds.child("title").getValue(String.class));
                    blogModel.setDate(ds.child("date").getValue(String.class));
                    blogModel.setImageUrl(ds.child("image").getValue(String.class));
                    blogModel.setTime(ds.child("time").getValue(String.class));
                    blogModel.setKey(ds.child("key").getValue(String.class));
                    bloglist.add(blogModel);
                }
             blogAdapter=new BlogAdapter(HomeActivity.this, bloglist, new BlogAdapter.DeleteListener() {
                    @Override
                    public void Litener(int position, String id) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Do you want to Delete this?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                        drefer.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                  //  bloglist.remove(position);
                                                   // blogAdapter.notifyItemRemoved(position);
                                                    Toasty.info(getApplicationContext(),"Delete Successful..",Toasty.LENGTH_SHORT).show();
                                                   // finish();
                                                }else {
                                                    Toasty.error(getApplicationContext(),"Delete failed..",Toasty.LENGTH_SHORT).show();
                                                   // finish();
                                                }
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //finish();
                                    }
                                }).show();
                        // return true;

                    }
                });
                newrecycler.setLayoutManager(new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.VERTICAL,false));
                newrecycler.setAdapter(blogAdapter);
                blogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pdfView() {
        DatabaseReference dbook=databaseReference.child("books");
        dbook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booklist.clear();
             //  int m=0;
                for (DataSnapshot ds:snapshot.getChildren()){
                    InfoModel inm=new InfoModel();
                    inm.setBookname(ds.child("bookname").getValue(String.class));
                    inm.setPdf_link(ds.child("pdf").getValue(String.class));
                   // m++;
                    booklist.add(inm);
//                    if (m==5){
//                        break;
//                    }
                }
                BookViewAdapter catagoryAdapter = new BookViewAdapter(HomeActivity.this,booklist);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(catagoryAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_home);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);}
                else {drawerLayout.openDrawer(GravityCompat.START);}
            }
        });
        animation();
    }

    private void animation() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.purple_500));
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    final float diffScaledoffset=slideOffset *(1-END_SCALE);
                    final float offsetScale=1-diffScaledoffset;
                    contentView.setScaleX(offsetScale);
                    contentView.setScaleY(offsetScale);

                    final float xOffset=drawerView.getWidth() *slideOffset;
                    final float xOffsetDiff=contentView.getWidth() *diffScaledoffset/2;
                    final float xTranslation=xOffset-xOffsetDiff;
                    contentView.setTranslationX(xTranslation);

                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void newsarticle() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Showing progress dialog before making http request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString() + "Network Error", Toast.LENGTH_SHORT).show();

                requestQueue.stop();
            }

        });
        requestQueue.add(stringRequest);


    }

    private void fetch() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CatagoryModel catagoryModel = new CatagoryModel();
                    catagoryModel.setPaperName(ds.child("paperName").getValue(String.class));
                    catagoryModel.setPaperImage(ds.child("paperImage").getValue(String.class));
                    catagoryModel.setShortDes(ds.child("shortDes").getValue(String.class));
                    catagoryModel.setPaperLink(ds.child("paperLink").getValue(String.class));
                    //catagoryModel.setRatings(ds.child("ratings").getValue(String.class));
                    list.add(catagoryModel);

                }
                CatagoryAdapter catagoryAdapter = new CatagoryAdapter(list, HomeActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(catagoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;

    }
}