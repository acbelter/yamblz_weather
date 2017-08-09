package me.maximpestryakov.yamblzweather.presentation.place;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import me.maximpestryakov.yamblzweather.data.db.model.PlaceData;
import me.maximpestryakov.yamblzweather.data.model.prediction.Prediction;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SelectPlaceView extends MvpView {
    void showFavoritePlaces(List<PlaceData> places);

    void showPlacePredictions(List<Prediction> predictions);

    @StateStrategyType(value = SkipStrategy.class)
    void showError();

    void close();
}
