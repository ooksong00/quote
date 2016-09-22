package young.com.sayingstory.ui.setting.setting_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import young.com.sayingstory.R;
import young.com.sayingstory.data.model.Setting;
import young.com.sayingstory.ui.constants.SharedPreferenceConst;
import young.com.sayingstory.ui.setting.about.AboutActivity;
import young.com.sayingstory.ui.setting.login.LoginDialogActivity;
import young.com.sayingstory.util.SharedPreferencesManager;

public class SettingRecyclerViewAdapter extends RecyclerView.Adapter<SettingRecyclerViewAdapter.ViewHolder> {

    private final static int ABOUT = 0;
    private final static int AlARM_SETTING = 1;
    private final static int LOGIN_SETTING = 2;
    private final static int EVALUATE = 3;

    private Context mContext;
    private List<Setting> settingsList;
    private final String TAG = "SettingRecycler";

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView status;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            title = (TextView) view.findViewById(R.id.setting_title);
            status = (TextView) view.findViewById(R.id.setting_status);
            view.setOnClickListener(this);
        }

        //event handler in recyclerview
        @Override
        public void onClick(View view) {
            switch (getPosition()) {
                case ABOUT :
                    Intent aboutIntent = new Intent(mContext, AboutActivity.class);
                    aboutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(aboutIntent);
                    break;
                case AlARM_SETTING :
                    if (SharedPreferencesManager.getBooleanValue(mContext, SharedPreferenceConst.IS_PUSH_MESSAGE) == true) {
                        Toast.makeText(mContext, mContext.getString(R.string.alarm_off_text), Toast.LENGTH_SHORT).show();
                        setPushOff();
                        status.setText(SharedPreferencesManager.getStringValue(mContext, SharedPreferenceConst.PUSH_STATUS_TEXT));
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.alarm_on_text), Toast.LENGTH_SHORT).show();
                        setPushOn();
                        status.setText(SharedPreferencesManager.getStringValue(mContext, SharedPreferenceConst.PUSH_STATUS_TEXT));
                    }

                    Log.d(TAG, "adapter " + SharedPreferencesManager.getStringValue(mContext, SharedPreferenceConst.PUSH_STATUS_TEXT));
                    Log.d(TAG, "adapter " + SharedPreferencesManager.getBooleanValue(mContext, SharedPreferenceConst.IS_PUSH_MESSAGE));
                    break;
                case LOGIN_SETTING :
                    Intent intent = new Intent(mContext, LoginDialogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    break;
                case EVALUATE :
                    final String appPackageName = mContext.getPackageName();
                    try{
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } catch (android.content.ActivityNotFoundException e) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    break;
            }
        }
    }

    public SettingRecyclerViewAdapter(Context context) {
        mContext = context;
        settingsList = new ArrayList<Setting>();
        settingsList.add(new Setting(mContext.getString(R.string.about), ""));
        settingsList.add(new Setting(mContext.getString(R.string.alarm_setting), SharedPreferencesManager.getStringValue(context, "pushStatusText")));
        settingsList.add(new Setting(mContext.getString(R.string.login), SharedPreferencesManager.getStringValue(context, SharedPreferenceConst.USER_NAME)));
        settingsList.add(new Setting(mContext.getString(R.string.evaluate), ""));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_recyclerview_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Setting setting = settingsList.get(position);
        holder.title.setText(setting.getTitle());
        holder.status.setText(setting.getStatus());
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public void setPushOff() {
        SharedPreferences.Editor editor = SharedPreferencesManager.getSharedPreferences(mContext).edit();
        editor.putBoolean(SharedPreferenceConst.IS_PUSH_MESSAGE, false);
        SharedPreferencesManager.setStringValue(mContext, SharedPreferenceConst.PUSH_STATUS_TEXT, mContext.getString(R.string.alarm_off));
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/alramOn");
        //FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/alramOnTest");
        editor.commit();
    }

    public void setPushOn() {
        SharedPreferences.Editor editor = SharedPreferencesManager.getSharedPreferences(mContext).edit();
        editor.putBoolean(SharedPreferenceConst.IS_PUSH_MESSAGE, true);
        SharedPreferencesManager.setStringValue(mContext, SharedPreferenceConst.PUSH_STATUS_TEXT, mContext.getString(R.string.alarm_on));
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/alramOn");
       // FirebaseMessaging.getInstance().subscribeToTopic("/topics/alramOnTest");
        editor.commit();
    }
}
