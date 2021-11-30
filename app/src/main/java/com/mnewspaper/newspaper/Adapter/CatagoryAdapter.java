package com.mnewspaper.newspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.mnewspaper.newspaper.LoginActivity;
import com.mnewspaper.newspaper.Model.CatagoryModel;
import com.mnewspaper.newspaper.R;

import java.util.List;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.ViewHolder> {

    public CatagoryAdapter(List<CatagoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private List<CatagoryModel> list;
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.most_viewed_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CatagoryModel ct=list.get(position);
        Glide.with(context).load(ct.getPaperImage()).placeholder(R.drawable.applogo).into(holder.imageView);
        holder.textView.setText(ct.getPaperName());
        holder.shoDes.setText(ct.getShortDes());
       //float ratingd=Float.parseFloat(ct.getRatings());
       // holder.rating.setRating(Float.parseFloat(ct.getRatings()) );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, LoginActivity.class);
                intent.putExtra("paperLink",ct.getPaperLink());
                context.startActivity(intent);
                Animatoo.animateFade(context);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,shoDes;
        RatingBar rating;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.mv_image);
            textView=itemView.findViewById(R.id.mv_title);
            shoDes=itemView.findViewById(R.id.mv_desc);
            rating=itemView.findViewById(R.id.mv_rating);
            cardView=itemView.findViewById(R.id.most_viewed_card);

        }
    }
}
