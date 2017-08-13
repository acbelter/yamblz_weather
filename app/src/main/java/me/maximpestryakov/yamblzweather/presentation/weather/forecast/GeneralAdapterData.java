package me.maximpestryakov.yamblzweather.presentation.weather.forecast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import me.maximpestryakov.yamblzweather.data.model.forecast.ForecastItem;
import me.maximpestryakov.yamblzweather.presentation.weather.TimeTag;

public class GeneralAdapterData {
    private List<GeneralForecastItem> items;
    private LinkedHashMap<String, List<ForecastItem>> itemsMap;

    public GeneralAdapterData(List<ForecastItem> forecast) {
        items = new ArrayList<>();
        itemsMap = new LinkedHashMap<>();

        if (forecast.isEmpty()) {
            return;
        }

        GeneralForecastItem itemCandidate = null;
        String currentDateTag = forecast.get(0).getDateTag();
        for (ForecastItem item : forecast) {
            // New tag
            if (!item.getDateTag().equals(currentDateTag)) {
                currentDateTag = item.getDateTag();
                itemCandidate = null;
            }

            if (itemCandidate == null) {
                itemCandidate = new GeneralForecastItem(item);
                items.add(itemCandidate);
            } else if (TimeTag.TIME_12.equals(item.getTimeTag())) {
                // Take weather at noon
                int index = items.indexOf(itemCandidate);
                itemCandidate = new GeneralForecastItem(item);
                items.set(index, itemCandidate);
            }

            if (!itemsMap.containsKey(item.getDateTag())) {
                itemsMap.put(item.getDateTag(), new ArrayList<>());
            }
            itemsMap.get(item.getDateTag()).add(item);
        }
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
