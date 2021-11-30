package com.mnewspaper.newspaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mnewspaper.newspaper.LocationOwner.DashboardFragment;
import com.mnewspaper.newspaper.LocationOwner.ManageFragment;
import com.mnewspaper.newspaper.LocationOwner.ProfileFragment;

public class DashboardRetailar_Activity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard_retailar_);

        chipNavigationBar=(ChipNavigationBar)findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashbord,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();

        bottomMenu();
    }


    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.bottom_nav_dashbord:
                        fragment=new DashboardFragment();
                        break;

                    case R.id.bottom_nav_manage:
                        fragment=new ManageFragment();
                        break;
                    case R.id.bottom_nav_profile:
                    fragment=new ProfileFragment();
                    break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }
        });

    }
}