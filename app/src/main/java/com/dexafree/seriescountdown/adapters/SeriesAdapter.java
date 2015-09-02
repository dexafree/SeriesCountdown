package com.dexafree.seriescountdown.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.seriescountdown.R;
import com.dexafree.seriescountdown.model.Serie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Carlos on 1/9/15.
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private List<Serie> seriesList;

    public SeriesAdapter(List<Serie> seriesList) {
        this.seriesList = seriesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.serie_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Serie item = seriesList.get(position);

        holder.text.setText(item.getName());
        holder.image.setImageBitmap(null);

        Picasso.with(holder.image.getContext())
                .cancelRequest(holder.image);

        Picasso.with(holder.image.getContext())
                .load(item.getImageUrl())
                .fit().centerCrop()
                .into(holder.image);

        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }


    public void addItem(Serie serie){
        seriesList.add(serie);
        notifyItemInserted(seriesList.size()-1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.serie_image);
            text = (TextView) itemView.findViewById(R.id.serie_name);
        }
    }
}
