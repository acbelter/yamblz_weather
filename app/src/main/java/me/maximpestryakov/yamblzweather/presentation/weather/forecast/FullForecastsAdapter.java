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

public class FullForecastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_GENERAL_FORECAST = 0;
    private static final int TYPE_FORECAST = 1;

    private GeneralAdapterData generalAdapterData;
    private List<Object> items;
    private AdapterCallback callback;

    @Inject
    DataFormatter formatter;

    public interface AdapterCallback {
        void onItemClicked(GeneralForecastItem item);
    }

    public FullForecastsAdapter(List<ForecastItem> forecast) {
        App.getAppComponent().inject(this);
        generalAdapterData = new GeneralAdapterData(forecast);
        items = new ArrayList<>(generalAdapterData.getItems());
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

    public static class DetailedForecastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.morningWeatherTitle)
        public TextView morningWeatherTitle;
        @BindView(R.id.dayWeatherTitle)
        public TextView dayWeatherTitle;
        @BindView(R.id.eveningWeatherTitle)
        public TextView eveningWeatherTitle;
        @BindView(R.id.nightWeatherTitle)
        public TextView nightWeatherTitle;

        @BindView(R.id.morningWeatherImage)
        public ImageView morningWeatherImage;
        @BindView(R.id.dayWeatherImage)
        public ImageView dayWeatherImage;
        @BindView(R.id.eveningWeatherImage)
        public ImageView eveningWeatherImage;
        @BindView(R.id.nightWeatherImage)
        public ImageView nightWeatherImage;

        @BindView(R.id.morningTemperature)
        public TextView morningTemperature;
        @BindView(R.id.dayTemperature)
        public TextView dayTemperature;
        @BindView(R.id.eveningTemperature)
        public TextView eveningTemperature;
        @BindView(R.id.nightTemperature)
        public TextView nightTemperature;

        @BindView(R.id.cloudiness)
        public TextView cloudiness;
        @BindView(R.id.wind)
        public TextView wind;
        @BindView(R.id.humidity)
        public TextView humidity;
        @BindView(R.id.pressure)
        public TextView pressure;

        public DetailedForecastViewHolder(View view) {
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
        if (item instanceof List) {
            return TYPE_FORECAST;
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
            case TYPE_FORECAST:
                view = inflater.inflate(R.layout.item_forecast_detailed, parent, false);
                return new DetailedForecastViewHolder(view);
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
            case TYPE_FORECAST:
                bindDetailedViewHolder((DetailedForecastViewHolder) holder, (List<ForecastItem>) item);
                break;
        }
    }

    private void bindGeneralViewHolder(GeneralForecastViewHolder holder,
                                       GeneralForecastItem item) {
        holder.dateTime.setText(formatter.getDate(item));
        String temperature = holder.itemView.getResources()
                .getString(R.string.temperature_value, formatter.getTemperatureC(item));
        holder.temperature.setText(temperature);
        holder.weatherImage.setImageResource(formatter.getWeatherImageDrawableId(item));

        holder.itemView.setOnClickListener(view -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos != RecyclerView.NO_POSITION) {
                if (adapterPos == items.size() - 1 ||
                        items.get(adapterPos + 1) instanceof GeneralForecastItem) {
                    Timber.d("NOT SHOWED " + adapterPos);
                    // Detailed forecast isn't showed
                    GeneralForecastItem currentItem = (GeneralForecastItem) items.get(adapterPos);
                    items.add(adapterPos + 1, generalAdapterData.getForecastItems(currentItem.dateTag));
                    notifyItemInserted(adapterPos + 1);
                } else {
                    Timber.d("SHOWED " + adapterPos);
                    // Detailed forecast is showed
                    items.remove(adapterPos + 1);
                    notifyItemRemoved(adapterPos + 1);
                }
                if (callback != null) {
                    GeneralForecastItem clickedItem =
                            generalAdapterData.getGeneralItem(adapterPos);
                    callback.onItemClicked(clickedItem);
                }
            }
        });
    }

    private void bindDetailedViewHolder(DetailedForecastViewHolder holder,
                                        List<ForecastItem> forecast) {
        Resources res = holder.itemView.getResources();
        bindDayForecast(res, holder, forecast);

        int averageCloudiness = 0;
        int averageWind = 0;
        int averageHumidity = 0;
        int averagePressure = 0;
        for (ForecastItem item : forecast) {
            averageCloudiness += formatter.getCloudiness(item);
            averageWind += formatter.getWind(item);
            averageHumidity += formatter.getHumidity(item);
            averagePressure += formatter.getPressure(item);
        }

        if (forecast.size() != 0) {
            averageCloudiness /= forecast.size();
            averageWind /= forecast.size();
            averageHumidity /= forecast.size();
            averagePressure /= forecast.size();
        }

        holder.cloudiness.setText(res.getString(R.string.cloudiness_value, averageCloudiness));
        holder.wind.setText(res.getString(R.string.wind_value, averageWind));
        holder.humidity.setText(res.getString(R.string.humidity_value, averageHumidity));
        holder.pressure.setText(res.getString(R.string.pressure_value, averagePressure));
    }

    private void bindDayForecast(Resources res,
                                 DetailedForecastViewHolder holder,
                                 List<ForecastItem> forecast) {
        AverageWeather[] weathers = formatter.getAverageWeathers(forecast);
        // Bind morning weather
        AverageWeather mw = weathers[0];
        if (mw != null) {
            holder.morningWeatherImage.setImageResource(
                    formatter.getWeatherImageDrawableId(mw.worstWeatherType));
            holder.morningTemperature.setText(
                    res.getString(R.string.temperature_value,
                            formatter.convertToC(mw.getAverageTemperature())));
        }
        holder.morningWeatherTitle.setVisibility(mw != null ? View.VISIBLE : View.GONE);
        holder.morningWeatherImage.setVisibility(mw != null ? View.VISIBLE : View.GONE);
        holder.morningTemperature.setVisibility(mw != null ? View.VISIBLE : View.GONE);

        // Bind day weather
        AverageWeather dw = weathers[1];
        if (dw != null) {
            holder.dayWeatherImage.setImageResource(
                    formatter.getWeatherImageDrawableId(dw.worstWeatherType));
            holder.dayTemperature.setText(
                    res.getString(R.string.temperature_value,
                            formatter.convertToC(dw.getAverageTemperature())));
        }
        holder.dayWeatherTitle.setVisibility(dw != null ? View.VISIBLE : View.GONE);
        holder.dayWeatherImage.setVisibility(dw != null ? View.VISIBLE : View.GONE);
        holder.dayTemperature.setVisibility(dw != null ? View.VISIBLE : View.GONE);

        // Bind evening weather
        AverageWeather ew = weathers[2];
        if (ew != null) {
            holder.eveningWeatherImage.setImageResource(
                    formatter.getWeatherImageDrawableId(ew.worstWeatherType));
            holder.eveningTemperature.setText(
                    res.getString(R.string.temperature_value,
                            formatter.convertToC(ew.getAverageTemperature())));
        }
        holder.eveningWeatherTitle.setVisibility(ew != null ? View.VISIBLE : View.GONE);
        holder.eveningWeatherImage.setVisibility(ew != null ? View.VISIBLE : View.GONE);
        holder.eveningTemperature.setVisibility(ew != null ? View.VISIBLE : View.GONE);

        // Bind evening weather
        AverageWeather nw = weathers[3];
        if (nw != null) {
            // Fix weather type for night
            holder.nightWeatherImage.setImageResource(
                    formatter.getWeatherImageDrawableId(nw.worstWeatherType));
            holder.nightTemperature.setText(
                    res.getString(R.string.temperature_value,
                            formatter.convertToC(nw.getAverageTemperature())));
        }
        holder.nightWeatherTitle.setVisibility(nw != null ? View.VISIBLE : View.GONE);
        holder.nightWeatherImage.setVisibility(nw != null ? View.VISIBLE : View.GONE);
        holder.nightTemperature.setVisibility(nw != null ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
