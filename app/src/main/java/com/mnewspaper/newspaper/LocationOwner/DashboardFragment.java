package com.mnewspaper.newspaper.LocationOwner;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.mnewspaper.newspaper.Admin.Blog_UploadActivity;
import com.mnewspaper.newspaper.Admin.BookUploadActivity;
import com.mnewspaper.newspaper.LinkUpload;
import com.mnewspaper.newspaper.R;

public class DashboardFragment extends Fragment {
    View view;
    private CardView addbook,addblog,addlink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
   view= inflater.inflate(R.layout.fragment_dashboard, container, false);
   identify();
   return view;
    }

    private void identify() {
        addbook=(CardView)view.findViewById(R.id.addbook);
        addblog=(CardView)view.findViewById(R.id.addblog);
        addlink=view.findViewById(R.id.addlink);
        addlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LinkUpload.class));
                Animatoo.animateFade(getContext());
            }
        });
        addblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Blog_UploadActivity.class));
                Animatoo.animateFade(getContext());
            }
        });
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BookUploadActivity.class));
                Animatoo.animateFade(getContext());
            }
        });

    }
}