package com.mnewspaper.newspaper.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mnewspaper.newspaper.Model.LinkModel;
import com.mnewspaper.newspaper.R;

import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {

    private Context context;
    private List<LinkModel> list;
    public LinkAdapter(Context context, List<LinkModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.link_card_views,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LinkModel linkModel=list.get(position);
            holder.linkname.setText(linkModel.getLinkname().toString());
            holder.linkurl.setText(linkModel.getLink().toString());
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Share Link");
                    String app_url = linkModel.getLink().toString();
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            });
            String mm=linkModel.getLink().toString();
            holder.copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", linkModel.getLink().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context,"Copied..",Toast.LENGTH_SHORT).show();
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mm));
                    context.startActivity(browserIntent);
                }
            });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView linkname;
        private TextView linkurl;
        private ImageView share,copy;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linkname=(TextView)itemView.findViewById(R.id.linkname);
            linkurl=(TextView)itemView.findViewById(R.id.linkurl);
            share=(ImageView)itemView.findViewById(R.id.shareurl);
            copy=(ImageView)itemView.findViewById(R.id.copyurl);
            cardView=(CardView)itemView.findViewById(R.id.cardViewforLink);
        }
    }
}
