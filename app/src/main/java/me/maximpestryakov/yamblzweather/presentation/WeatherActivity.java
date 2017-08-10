package me.maximpestryakov.yamblzweather.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.presentation.place.PlacesAdapter;
import me.maximpestryakov.yamblzweather.presentation.place.SelectPlaceActivity;
import me.maximpestryakov.yamblzweather.presentation.settings.SettingsActivity;
import me.maximpestryakov.yamblzweather.util.UiUtil;

public class WeatherActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.weatherImage)
    ImageView weatherImage;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.dayOfWeek)
    TextView dayOfWeek;
    @BindView(R.id.humidity)
    TextView humidity;
    @BindView(R.id.wind)
    TextView wind;
    @BindView(R.id.clouds)
    TextView clouds;
    @BindView(R.id.pressure)
    TextView pressure;
    @BindView(R.id.forecast)
    RecyclerView forecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);

        UiUtil.toolbar(this, toolbar, true);

        toolbar.setNavigationIcon(R.drawable.ic_select_place);
        toolbar.setNavigationContentDescription(R.string.select_place);
        toolbar.setNavigationOnClickListener(view -> showSelectPlaceUi());

        title.setOnClickListener(view -> showSelectPlaceUi());

        forecasts.setHasFixedSize(true);
        forecasts.setLayoutManager(new LinearLayoutManager(this));

        initTestUi();
    }

    private void initTestUi() {
        title.setText("Moscow");
        weatherImage.setImageResource(R.drawable.sunny_day);
        temperature.setText("+21°");
        dayOfWeek.setText("Sunday");
        date.setText("21 July 2017");
        humidity.setText("50%");
        wind.setText("21 m/s");
        clouds.setText("30%");
        pressure.setText("123 kPa");

        List<PlaceData> places = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            PlaceData data = new PlaceData(Integer.toString(i));
            data.placeName = "Place name " + i;
            places.add(data);
        }

        forecasts.setAdapter(new PlacesAdapter(places));
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
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_refresh: {
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

    private void showSelectPlaceUi() {
        Intent intent = new Intent(WeatherActivity.this, SelectPlaceActivity.class);
        intent.putExtra(Consts.KEY_SELECT_FIRST_PLACE, true);
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
                    Toast.makeText(this, selectedPlace.placeName, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
