package me.maximpestryakov.yamblzweather.presentation.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {
    void setUpdateIntervalUiEnabled(boolean enabled);

    void setupUpdateIntervalUi(int maxPos);

    void showUpdateInterval(int pos, String posStrValue);

    void close();
}
