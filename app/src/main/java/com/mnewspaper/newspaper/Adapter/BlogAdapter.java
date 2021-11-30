package com.mnewspaper.newspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mnewspaper.newspaper.BlogViewActivity;
import com.mnewspaper.newspaper.Model.Article;
import com.mnewspaper.newspaper.Model.BlogModel;
import com.mnewspaper.newspaper.R;
import com.mnewspaper.newspaper.Utils;

import java.time.temporal.Temporal;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private Context context;


    private DeleteListener listener;

    public BlogAdapter(Context context, List<BlogModel> list ,DeleteListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    private List<BlogModel> list;
    @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_views_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
        BlogModel article=list.get(position);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();


        Glide.with(context).load(article.getImageUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.ttitle.setText(article.getBtitle());
        holder.tdes.setText(article.getBdes());
        holder.tdate.setText(Utils.DateFormat(article.getDate()));
        holder.tsource.setText(" \u2022"+ Utils.DateToTimeFormat(article.getDate()));
        String key=article.getKey();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BlogViewActivity.class);
                intent.putExtra("date",article.getDate());
                intent.putExtra("title",article.getBtitle());
                intent.putExtra("image",article.getImageUrl());
                intent.putExtra("description",article.getBdes());
               context.startActivity(intent);
                Animatoo.animateFade(context);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.Litener(position,key);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tdate,ttitle,tdes,tsource;
        ProgressBar progressBar;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tdate=itemView.findViewById(R.id.dateinnews);
            ttitle=itemView.findViewById(R.id.title);
            tdes=itemView.findViewById(R.id.desc);
            imageView=itemView.findViewById(R.id.img);
            progressBar=itemView.findViewById(R.id.progress_load_photo);
            tsource=itemView.findViewById(R.id.souce);
            cardView=itemView.findViewById(R.id.cardDesign);
        }
    }
    public interface DeleteListener{
        void Litener(int position,String id);
    }
}
