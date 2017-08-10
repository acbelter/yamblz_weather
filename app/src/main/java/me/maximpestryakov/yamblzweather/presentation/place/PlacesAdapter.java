package me.maximpestryakov.yamblzweather.presentation.place;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<PlaceData> places;
    private AdapterCallback callback;

    public interface AdapterCallback {
        void onItemRemoved(PlaceData place);
        void onItemClicked(PlaceData place);
    }

    public PlacesAdapter(List<PlaceData> places) {
        this.places = places;
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    public void removeAdapterCallback() {
        this.callback = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeName)
        public TextView placeName;
        @BindView(R.id.deletePlaceImage)
        public ImageView deletePlaceImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.placeName.setText(places.get(position).placeName);
        holder.deletePlaceImage.setOnClickListener(view -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                PlaceData place = places.get(adapterPos);
                places.remove(adapterPos);
                notifyItemRemoved(adapterPos);
                notifyItemChanged(0);
                if (callback != null) {
                    callback.onItemRemoved(place);
                }
            }
        });

        holder.itemView.setOnClickListener(view -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (callback != null) {
                    callback.onItemClicked(places.get(adapterPos));
                }
            }
        });

        if (places.size() == 1) {
            holder.deletePlaceImage.setVisibility(View.INVISIBLE);
        } else {
            holder.deletePlaceImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
