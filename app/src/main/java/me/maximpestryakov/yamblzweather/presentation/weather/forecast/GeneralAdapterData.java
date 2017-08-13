package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.presentation.weather.TimeTag;
import timber.log.Timber;

public class GeneralAdapterData {
    private List<GeneralForecastItem> items;
    private HashMap<String, List<ForecastItem>> itemsMap;

    public GeneralAdapterData(List<ForecastItem> forecast) {
        items = new ArrayList<>();
        itemsMap = new HashMap<>();

        if (forecast.isEmpty()) {
            return;
        }

        String currentDateTag = forecast.get(0).getDateTag();
        boolean inCurrentItemInitialized = false;

        for (ForecastItem item : forecast) {
            // Take first item with current date tag
            if (currentDateTag.equals(item.getDateTag())) {
                if (inCurrentItemInitialized) {
                    continue;
                }
                inCurrentItemInitialized = true;
                items.add(new GeneralForecastItem(item.getDateTag(), item.getWeather(),
                        item.main.temp, item.dataTimestamp));
            } else if (TimeTag.TIME_12.equals(item.getTimeTag())) {
                // Take weather at noon
                items.add(new GeneralForecastItem(item.getDateTag(), item.getWeather(),
                        item.main.temp, item.dataTimestamp));
            }

            if (!itemsMap.containsKey(item.getDateTag())) {
                itemsMap.put(item.getDateTag(), new ArrayList<>());
            }
            itemsMap.get(item.getDateTag()).add(item);
        }
        Timber.d("General items count: %s", items.size());
    }

    public int getItemCount() {
        return items.size();
    }

    public List<GeneralForecastItem> getItems() {
        return items;
    }

    public GeneralForecastItem getGeneralItem(int position) {
        return items.get(position);
    }

    public List<ForecastItem> getForecastItems(String dateTag) {
        return itemsMap.get(dateTag);
    }
}
