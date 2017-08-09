package me.maximpestryakov.yamblzweather.presentation.place;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<PlaceData> places;

    public PlacesAdapter(List<PlaceData> places) {
        this.places = places;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.placeName)
        public TextView placeName;

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
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
