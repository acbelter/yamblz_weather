package me.maximpestryakov.yamblzweather.data.model;

public enum WeatherType {
    NIGHT(0),
    NIGHT_CLOUDS(1),
    SUN(2),
    CLOUDS(3),
    RAIN(4),
    SNOW(5),
    STORM(6);

    private final int priority;

    WeatherType(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
