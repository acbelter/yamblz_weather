package me.maximpestryakov.yamblzweather.data;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.data.model.WeatherType;
import me.maximpestryakov.yamblzweather.data.model.common.Weather;
import me.maximpestryakov.yamblzweather.di.DaggerTestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppModule;
import me.maximpestryakov.yamblzweather.presentation.DataFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DataFormatterTest {
    @Mock
    Context mockContext;
    @Inject
    DataFormatter formatter;

    @Before
    public void setUp() {
        TestAppModule testAppModule = new TestAppModule(mockContext);

        TestAppComponent component = DaggerTestAppComponent.builder()
                .appModule(testAppModule)
                .build();
        component.inject(this);
    }

    @Test
    public void testGeneralConverters() {
        assertThat(formatter.convertToC(273.15f)).isEqualTo(0);
        assertThat(formatter.convertToC(294.15f)).isEqualTo(21);
    }

    @Test
    public void testWeatherTypeFromWeatherIcon() {
        Weather weather = null;
        assertThat(formatter.getWeatherType(weather)).isNull();

        weather = new Weather();
        weather.icon = "123";
        assertThat(formatter.getWeatherType(weather)).isNull();

        String[] weatherIcons = new String[]{
                "01d", "01n",
                "02d", "02n",
                "03d", "09n",
                "04d", "04n",
                "09d", "09n",
                "10d", "10n",
                "11d", "11n",
                "13d", "13n",
                "50d", "50n",
        };

        WeatherType type;
        for (String icon : weatherIcons) {
            weather.icon = icon;
            type = formatter.getWeatherType(weather);

            switch (icon) {
                case "01d":
                    assertThat(type).isEqualTo(WeatherType.SUN);
                    break;
                case "01n":
                    assertThat(type).isEqualTo(WeatherType.NIGHT);
                    break;
                case "02d":
                    assertThat(type).isEqualTo(WeatherType.CLOUDS);
                    break;
                case "02n":
                    assertThat(type).isEqualTo(WeatherType.NIGHT_CLOUDS);
                    break;
                case "03d":
                    assertThat(type).isEqualTo(WeatherType.CLOUDS);
                    break;
                case "03n":
                    assertThat(type).isEqualTo(WeatherType.NIGHT_CLOUDS);
                    break;
                case "04d":
                    assertThat(type).isEqualTo(WeatherType.CLOUDS);
                    break;
                case "04n":
                    assertThat(type).isEqualTo(WeatherType.NIGHT_CLOUDS);
                    break;
                case "09d":
                    assertThat(type).isEqualTo(WeatherType.RAIN);
                    break;
                case "09n":
                    assertThat(type).isEqualTo(WeatherType.RAIN);
                    break;
                case "10d":
                    assertThat(type).isEqualTo(WeatherType.RAIN);
                    break;
                case "10n":
                    assertThat(type).isEqualTo(WeatherType.RAIN);
                    break;
                case "11d":
                    assertThat(type).isEqualTo(WeatherType.STORM);
                    break;
                case "11n":
                    assertThat(type).isEqualTo(WeatherType.STORM);
                    break;
                case "13d":
                    assertThat(type).isEqualTo(WeatherType.SNOW);
                    break;
                case "13n":
                    assertThat(type).isEqualTo(WeatherType.SNOW);
                    break;
                case "50d":
                    assertThat(type).isEqualTo(WeatherType.SUN);
                    break;
                case "50n":
                    assertThat(type).isEqualTo(WeatherType.NIGHT);
                    break;
                default:
                    assertThat(type).isNull();
                    break;
            }
        }
    }

    @Test
    public void testWeatherDrawableFromWeatherType() {
        for (WeatherType type : WeatherType.values()) {
            int id = formatter.getWeatherImageDrawableId(type);
            switch (type) {
                case NIGHT:
                    assertThat(id).isEqualTo(R.drawable.night);
                    break;
                case NIGHT_CLOUDS:
                    assertThat(id).isEqualTo(R.drawable.night_clouds);
                    break;
                case SUN:
                    assertThat(id).isEqualTo(R.drawable.sun);
                    break;
                case CLOUDS:
                    assertThat(id).isEqualTo(R.drawable.clouds);
                    break;
                case RAIN:
                    assertThat(id).isEqualTo(R.drawable.rain);
                    break;
                case SNOW:
                    assertThat(id).isEqualTo(R.drawable.snow);
                    break;
                case STORM:
                    assertThat(id).isEqualTo(R.drawable.storm);
                    break;
                default:
                    assertThat(id).isEqualTo(0);
                    break;
            }
        }
    }

    @Test
    public void testWeatherResult() {

    }

    @Test
    public void testForecastItem() {

    }

    @Test
    public void testGeneralForecastItem() {

    }
}
