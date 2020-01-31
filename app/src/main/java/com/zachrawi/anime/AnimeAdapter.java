package com.zachrawi.anime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    private Context mContext;
    private int mResource;
    private ArrayList<Anime> mAnimes;
    private OnClickListener mOnClickListener;

    public AnimeAdapter(Context context, int resource, ArrayList<Anime> animes, OnClickListener onClickListener) {
        mContext = context;
        mResource = resource;
        mAnimes = animes;
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(mResource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Anime anime = mAnimes.get(position);

        Picasso
                .with(mContext)
                .load(anime.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivPoster);

        holder.tvTitle.setText(anime.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

    public interface OnClickListener {
        void onClick(int position);
    }
}
