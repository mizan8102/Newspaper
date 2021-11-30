package com.mnewspaper.newspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.mnewspaper.newspaper.BookViewActivity;
import com.mnewspaper.newspaper.Model.InfoModel;
import com.mnewspaper.newspaper.R;

import java.util.List;

public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.MyViewHolder> {
    private Context context;

    public BookViewAdapter(Context context, List<InfoModel> list) {
        this.context = context;
        this.list = list;
    }

    private List<InfoModel> list;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.book_view_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InfoModel infoModel=list.get(position);
        holder.textView.setText(infoModel.getBookname());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BookViewActivity.class);
                intent.putExtra("pdfbook",infoModel.getPdf_link());
                context.startActivity(intent);
                Animatoo.animateFade(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.bookname);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.bookviewintent);
        }
    }
}
