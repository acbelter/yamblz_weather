package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.presentation.DataFormatter;

public class GeneralForecastsAdapter extends
        RecyclerView.Adapter<GeneralForecastsAdapter.ViewHolder> {
    private List<GeneralForecastItem> items;
    private HashMap<String, List<ForecastItem>> itemsMap;
    private AdapterCallback callback;

    @Inject
    DataFormatter formatter;

    public interface AdapterCallback {
        void onItemClicked(GeneralForecastItem item, List<ForecastItem> forecast);
    }

    public GeneralForecastsAdapter(List<ForecastItem> forecast) {
        App.getAppComponent().inject(this);
        items = new ArrayList<>();
        itemsMap = new HashMap<>();

        for (ForecastItem item : forecast) {
            // Example of string to split: "2017-01-30 11:00:00"
            String[] tags = item.dataTimestampStr.split(" ");
            // Average weather is at noon
            if ("12:00:00".equals(tags[1])) {
                items.add(new GeneralForecastItem(
                        tags[0], item.getWeather(), item.main.temp, item.dataTimestamp));
            }

            if (!itemsMap.containsKey(tags[0])) {
                itemsMap.put(tags[0], new ArrayList<>());
            }
            itemsMap.get(tags[0]).add(item);
        }
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    public void removeAdapterCallback() {
        this.callback = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        public TextView date;
        @BindView(R.id.temperature)
        public TextView temperature;
        @BindView(R.id.weatherImage)
        public ImageView weatherImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_general, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GeneralForecastItem item = items.get(position);
        holder.date.setText(formatter.getDate(item));
        String temperature = holder.itemView.getResources()
                .getString(R.string.temperature_value, formatter.getTemperatureC(item));
        holder.temperature.setText(temperature);
        holder.weatherImage.setImageResource(formatter.getWeatherImageDrawableId(item));

        holder.itemView.setOnClickListener(view -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (callback != null) {
                    GeneralForecastItem clickedItem = items.get(adapterPos);
                    callback.onItemClicked(clickedItem, itemsMap.get(clickedItem.dateTag));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
