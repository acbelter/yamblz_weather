package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.presentation.DataFormatter;
import timber.log.Timber;

public class DetailedForecastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GENERAL_FORECAST = 0;
    private static final int TYPE_FORECAST_DETAILS = 1;

    private List<Object> items;
    private AdapterCallback callback;

    @Inject
    DataFormatter formatter;

    public interface AdapterCallback {
        void onItemClicked(GeneralForecastItem item);
    }

    public DetailedForecastsAdapter(List<ForecastItem> forecast) {
        App.getAppComponent().inject(this);
        items = new ArrayList<>(forecast.size());

        for (ForecastItem item : forecast) {
            items.add(new GeneralForecastItem(item));
        }
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    public void removeAdapterCallback() {
        this.callback = null;
    }

    public static class GeneralForecastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dateTime)
        public TextView dateTime;
        @BindView(R.id.temperature)
        public TextView temperature;
        @BindView(R.id.weatherImage)
        public ImageView weatherImage;

        public GeneralForecastViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ForecastDetailsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cloudiness)
        public TextView cloudiness;
        @BindView(R.id.wind)
        public TextView wind;
        @BindView(R.id.humidity)
        public TextView humidity;
        @BindView(R.id.pressure)
        public TextView pressure;

        public ForecastDetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof GeneralForecastItem) {
            return TYPE_GENERAL_FORECAST;
        }
        if (item instanceof ForecastItem) {
            return TYPE_FORECAST_DETAILS;
        }
        throw new UnsupportedOperationException("Unknown item view type");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case TYPE_GENERAL_FORECAST:
                view = inflater.inflate(R.layout.item_forecast_general, parent, false);
                return new GeneralForecastViewHolder(view);
            case TYPE_FORECAST_DETAILS:
                view = inflater.inflate(R.layout.item_forecast_details, parent, false);
                return new ForecastDetailsViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_GENERAL_FORECAST:
                bindGeneralViewHolder((GeneralForecastViewHolder) holder, (GeneralForecastItem) item);
                break;
            case TYPE_FORECAST_DETAILS:
                bindDetailsViewHolder((ForecastDetailsViewHolder) holder, (ForecastItem) item);
                break;
        }
    }

    private void bindGeneralViewHolder(GeneralForecastViewHolder holder,
                                       GeneralForecastItem item) {
        Timber.d("BIND " + item.dateTag + " " + item.timeTag + " " + formatter.getTime(item.item));
        holder.dateTime.setText(formatter.getTime(item.item));
        String temperature = holder.itemView.getResources()
                .getString(R.string.temperature_value, formatter.getTemperatureC(item));
        holder.temperature.setText(temperature);
        holder.weatherImage.setImageResource(formatter.getWeatherImageDrawableId(item));

        holder.itemView.setOnClickListener(view -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (adapterPos == items.size() - 1 ||
                        items.get(adapterPos + 1) instanceof GeneralForecastItem) {
                    // Forecast details are not showed
                    GeneralForecastItem currentItem = (GeneralForecastItem) items.get(adapterPos);
                    items.add(adapterPos + 1, currentItem.item);
                    notifyItemInserted(adapterPos + 1);
                } else {
                    // Detailed forecast is showed
                    items.remove(adapterPos + 1);
                    notifyItemRemoved(adapterPos + 1);
                }
                if (callback != null) {
                    callback.onItemClicked((GeneralForecastItem) items.get(adapterPos));
                }
            }
        });
    }

    private void bindDetailsViewHolder(ForecastDetailsViewHolder holder,
                                       ForecastItem item) {
        Resources res = holder.itemView.getResources();
        holder.cloudiness.setText(res.getString(R.string.cloudiness_value, formatter.getCloudiness(item)));
        holder.wind.setText(res.getString(R.string.wind_value, formatter.getWind(item)));
        holder.humidity.setText(res.getString(R.string.humidity_value, formatter.getHumidity(item)));
        holder.pressure.setText(res.getString(R.string.pressure_value, formatter.getPressure(item)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
