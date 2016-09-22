package young.com.sayingstory.ui.setting.setting_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import young.com.sayingstory.R;
import young.com.sayingstory.ui.event.LoginEvent;

public class SettingActivity extends AppCompatActivity {

    private final String TAG = "SettingActivity";
    private RecyclerView mSettingRecyclerview;
    private SettingRecyclerViewAdapter mSettingRecyclerViewAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        setRecyclerview();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Log.d(TAG, "Setting Activity onCreate");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.setting));
        mSettingRecyclerview = (RecyclerView) findViewById(R.id.setting_recyclerview);

        setRecyclerview();

    }

    private void setRecyclerview() {
        mSettingRecyclerViewAdapter = new SettingRecyclerViewAdapter(getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mSettingRecyclerview.setLayoutManager(mLayoutManager);
        mSettingRecyclerview.setAdapter(mSettingRecyclerViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
