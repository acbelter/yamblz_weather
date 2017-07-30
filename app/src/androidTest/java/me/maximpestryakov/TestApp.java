package me.maximpestryakov;

import me.maximpestryakov.yamblzweather.App;
import me.maximpestryakov.yamblzweather.di.AppComponent;
import me.maximpestryakov.yamblzweather.di.DaggerTestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppComponent;
import me.maximpestryakov.yamblzweather.di.TestAppModule;

import static android.support.test.InstrumentationRegistry.getTargetContext;

public class TestApp extends App {
    private TestAppComponent testAppComponent;
    private TestAppModule testAppModule;

    public TestAppModule getTestAppModule() {
        return testAppModule;
    }

    public TestAppComponent getTestAppComponent() {
        return testAppComponent;
    }

    @Override
    protected AppComponent initAppComponent() {
        testAppModule = new TestAppModule(getTargetContext());

        testAppComponent = DaggerTestAppComponent.builder()
                .appModule(testAppModule)
                .build();
        return testAppComponent;
    }
}
