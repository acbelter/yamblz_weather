package me.maximpestryakov.yamblzweather.presentation.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.FullWeatherData;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.data.model.weather.WeatherResult;
import me.maximpestryakov.yamblzweather.presentation.BaseActivity;
import me.maximpestryakov.yamblzweather.presentation.Consts;
import me.maximpestryakov.yamblzweather.presentation.DataFormatter;
import me.maximpestryakov.yamblzweather.presentation.place.SelectPlaceActivity;
import me.maximpestryakov.yamblzweather.presentation.settings.SettingsActivity;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.DetailedForecastsAdapter;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.FullForecastsAdapter;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralAdapterData;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralForecastItem;
import me.maximpestryakov.yamblzweather.presentation.weather.forecast.GeneralForecastsAdapter;
import me.maximpestryakov.yamblzweather.util.UiUtil;
import timber.log.Timber;

public class WeatherActivity extends BaseActivity implements
        WeatherView, GeneralForecastsAdapter.AdapterCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.weatherImage)
    ImageView weatherImage;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.dayOfWeek)
    TextView dayOfWeek;
    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.cloudinessImage)
    ImageView cloudinessImage;
    @BindView(R.id.windImage)
    ImageView windImage;
    @BindView(R.id.humidityImage)
    ImageView humidityImage;

    @BindView(R.id.cloudiness)
    TextView cloudiness;
    @BindView(R.id.wind)
    TextView wind;
    @BindView(R.id.humidity)
    TextView humidity;

    @BindView(R.id.forecast)
    RecyclerView forecast;

    RecyclerView forecastDetailed;

    @InjectPresenter
    WeatherPresenter presenter;

    @Inject
    DataFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        forecastDetailed = findViewById(R.id.forecastDetailed);

        UiUtil.toolbar(this, toolbar, true);

        toolbar.setNavigationIcon(R.drawable.ic_select_place);
        toolbar.setNavigationContentDescription(R.string.select_place);
        toolbar.setNavigationOnClickListener(view -> showSelectPlaceUi(false));

        title.setOnClickListener(view -> showSelectPlaceUi(false));

        forecast.setHasFixedSize(true);
        forecast.setLayoutManager(new LinearLayoutManager(this));

        if (forecastDetailed != null) {
            forecastDetailed.setHasFixedSize(true);
            forecastDetailed.setLayoutManager(new LinearLayoutManager(this));
        }

        presenter.updateCurrentPlaceWeather();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        boolean result = super.onCreateOptionsMenu(menu);
        UiUtil.tintToolbarIcons(toolbar, R.color.colorToolbarContent);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                presenter.updateCurrentPlaceWeather();
                return true;
            }
            case R.id.action_settings: {
                showSettingsUi();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void requestPlaceSelection() {
        showSelectPlaceUi(true);
    }

    @Override
    public void showLoading(boolean loading) {

    }

    @Override
    public void showPlaceName(String name) {
        title.setText(name);
    }

    @Override
    public void showWeather(FullWeatherData data) {
        Timber.d("Show weather. Use cache: %s", data.isFromCache());
        WeatherResult weather = data.getWeather();
        weatherImage.setImageResource(formatter.getWeatherImageDrawableId(weather));
        temperature.setText(getString(R.string.temperature_value, formatter.getTemperatureC(weather)));
        dayOfWeek.setText(formatter.getFormattedDayOfWeek(weather));
        date.setText(formatter.getFormattedDate(weather));

        cloudinessImage.setVisibility(View.VISIBLE);
        windImage.setVisibility(View.VISIBLE);
        humidityImage.setVisibility(View.VISIBLE);

        cloudiness.setText(getString(R.string.cloudiness_value, formatter.getCloudiness(weather)));
        wind.setText(getString(R.string.wind_value, formatter.getWind(weather)));
        humidity.setText(getString(R.string.humidity_value, formatter.getHumidity(weather)));

        if (forecastDetailed != null) {
            GeneralForecastsAdapter generalAdapter = new GeneralForecastsAdapter(data.getForecast());
            generalAdapter.setAdapterCallback(this);
            forecast.setAdapter(generalAdapter);

            GeneralAdapterData adapterData = generalAdapter.getData();
            if (adapterData.getItemCount() > 0) {
                List<ForecastItem> detailedForecastItems =
                        adapterData.getForecastItems(adapterData.getGeneralItem(0).dateTag);
                forecastDetailed.setAdapter(new DetailedForecastsAdapter(detailedForecastItems));
            }
        } else {
            FullForecastsAdapter fullAdapter = new FullForecastsAdapter(data.getForecast());
            forecast.setAdapter(fullAdapter);
        }
    }

    @Override
    public void showError(@StringRes int errorStrId) {
        Snackbar.make(findViewById(android.R.id.content), errorStrId, Snackbar.LENGTH_LONG).show();
    }

    private void showSelectPlaceUi(boolean forced) {
        Intent intent = new Intent(WeatherActivity.this, SelectPlaceActivity.class);
        intent.putExtra(Consts.KEY_SELECT_FIRST_PLACE, forced);
        startActivityForResult(intent, Consts.REQUEST_CODE_SELECT_PLACE);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showSettingsUi() {
        Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Consts.REQUEST_CODE_SELECT_PLACE:
                    PlaceData selectedPlace = data.getParcelableExtra(Consts.KEY_SELECTED_PLACE);
                    showPlaceName(selectedPlace.placeName);
                    presenter.updateCurrentPlace(selectedPlace.placeId);
                    presenter.updateCurrentPlaceWeather();
                    break;
            }
        }
    }

    @Override
    public void onItemClicked(GeneralForecastItem item, List<ForecastItem> forecast) {
        if (forecastDetailed != null) {
            forecastDetailed.setAdapter(new DetailedForecastsAdapter(forecast));
        }
    }
}
