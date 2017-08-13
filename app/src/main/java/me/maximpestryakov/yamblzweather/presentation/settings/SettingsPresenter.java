package me.maximpestryakov.yamblzweather.presentation.settings;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.PrefsRepository;
import me.maximpestryakov.yamblzweather.data.SyncWeatherJob;
import timber.log.Timber;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {
    @Inject
    Context context;
    @Inject
    PrefsRepository prefs;
    private final String[] updateIntervalTitles;
    private final int[] updateIntervalValues;

    private int updatePosition;
    private boolean updateBySchedule;
    private int updateInterval;

    public SettingsPresenter() {
        App.getAppComponent().inject(this);

        updateIntervalTitles = context.getResources().getStringArray(R.array.update_interval_titles);
        updateIntervalValues = context.getResources().getIntArray(R.array.update_interval_values);

        if (updateIntervalTitles.length != updateIntervalValues.length) {
            throw new AssertionError("Update interval titles count must be equals to values count");
        }
    }

    public void init() {
        updateBySchedule = prefs.isUpdateBySchedule();
        updateInterval = prefs.getUpdateInterval();

        Timber.d("Update by schedule: %s", updateBySchedule);
        Timber.d("Update interval %s", updateInterval);

        int pos = 0;
        for (int i = 0; i < updateIntervalValues.length; i++) {
            if (updateInterval == updateIntervalValues[i]) {
                pos = i;
                break;
            }
        }

        String posStrValue = updateIntervalTitles[pos];

        getViewState().showUpdateInterval(pos, posStrValue);
        getViewState().setUpdateIntervalUiEnabled(updateBySchedule);
    }

    public int getUpdateIntervalValuesCount() {
        return updateIntervalValues.length;
    }

    public void setUpdateBySchedule(boolean updateBySchedule) {
        getViewState().setUpdateIntervalUiEnabled(updateBySchedule);
        this.updateBySchedule = updateBySchedule;
    }

    public void setUpdateIntervalByPosition(int position) {
        updatePosition = position;
        updateInterval = updateIntervalValues[position];
        getViewState().showUpdateInterval(position, updateIntervalTitles[position]);
    }

    public void saveChanges() {
        updateInterval = updateIntervalValues[updatePosition];
        prefs.setUpdateBySchedule(updateBySchedule);
        prefs.setUpdateInterval(updateInterval);

        if (updateBySchedule) {
            SyncWeatherJob.schedule(updateInterval);
        }
    }
}
