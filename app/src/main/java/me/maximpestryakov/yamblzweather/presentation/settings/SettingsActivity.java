package me.maximpestryakov.yamblzweather.presentation.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.maximpestryakov.yamblzweather.R;
import me.maximpestryakov.yamblzweather.presentation.BaseActivity;
import me.maximpestryakov.yamblzweather.util.UiUtil;
import timber.log.Timber;

public class SettingsActivity extends BaseActivity implements SettingsView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.updateByScheduleSwitch)
    SwitchCompat updateByScheduleSwitch;
    @BindView(R.id.updateIntervalTitle)
    TextView updateIntervalTitle;
    @BindView(R.id.updateIntervalValue)
    TextView updateIntervalValue;
    @BindView(R.id.updateIntervalSeekBar)
    SeekBar updateIntervalSeekBar;
    @InjectPresenter
    SettingsPresenter presenter;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        UiUtil.toolbar(this, toolbar, true);
        title.setText(R.string.settings);

        presenter.init();

        updateIntervalSeekBar.setMax(presenter.getUpdateIntervalValuesCount() - 1);

        Disposable switchDisposable =
                RxCompoundButton
                        .checkedChanges(updateByScheduleSwitch)
                        .skipInitialValue()
                        .subscribe(checked -> {
                            Timber.d("Changed update interval state: %s", checked);
                            presenter.setUpdateBySchedule(checked);
                        });

        Disposable seekBarDisposable =
                RxSeekBar.userChanges(updateIntervalSeekBar)
                        .skipInitialValue()
                        .subscribe(position -> {
                            Timber.d("Changed update interval position: %s", position);
                            presenter.setUpdateIntervalByPosition(position);
                        });

        disposable = new CompositeDisposable(switchDisposable, seekBarDisposable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        UiUtil.tintToolbarIcons(toolbar, R.color.colorToolbarContent);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                close();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void setUpdateIntervalUiEnabled(boolean enabled) {
        updateByScheduleSwitch.setChecked(enabled);
        updateIntervalTitle.setEnabled(enabled);
        updateIntervalValue.setEnabled(enabled);
        updateIntervalSeekBar.setEnabled(enabled);
    }

    @Override
    public void setupUpdateIntervalUi(int maxPos) {
        updateIntervalSeekBar.setMax(maxPos);
    }

    @Override
    public void showUpdateInterval(int pos, String posStrValue) {
        updateIntervalSeekBar.setProgress(pos);
        updateIntervalValue.setText(posStrValue);
    }

    @Override
    public void close() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
