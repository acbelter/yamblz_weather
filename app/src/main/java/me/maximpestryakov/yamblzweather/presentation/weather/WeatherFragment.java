package me.maximpestryakov.yamblzweather.presentation.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.Weather;

public class WeatherFragment extends MvpAppCompatFragment implements WeatherView {

    private final DateFormat dateFormat = SimpleDateFormat.getTimeInstance();
    @InjectPresenter
    WeatherPresenter weatherPresenter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.time)
    TextView time;
    private Unbinder unbinder;

    @NonNull
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(weatherPresenter::onUpdateWeather);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.nav_weather);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showWeather(Weather weather) {
        temperature.setText(getString(R.string.temperature, Math.round(weather.getMain().getTemperature())));
        time.setText(getString(R.string.time, dateFormat.format(new Date(weather.getTime() * 1000))));
    }

    @Override
    public void setLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
