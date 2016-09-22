package young.com.sayingstory.ui.main.main_screen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import young.com.sayingstory.R;
import young.com.sayingstory.ui.constants.SharedPreferenceConst;
import young.com.sayingstory.ui.main.quote.QuoteFragment;
import young.com.sayingstory.ui.main.user_quote.UserQuoteFragment;
import young.com.sayingstory.ui.setting.setting_screen.SettingActivity;
import young.com.sayingstory.util.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";
    private FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private Class mClass[] = {QuoteFragment.class, UserQuoteFragment.class};
    private Fragment mFragment[] = {new QuoteFragment(), new UserQuoteFragment()};
    private String[]  mTitles ;
    private int mImages[] = {
            R.drawable.tab_home,
            R.drawable.tab_report
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        mTitles =  new String[] { getString(R.string.quote), getString(R.string.writing_saying) };

        //Set tab
        setTabView();
        setTabEvent();

        //is push acceptance or rejection
        if (SharedPreferencesManager.getBooleanValue(getApplicationContext(), SharedPreferenceConst.IS_PUSH_MESSAGE)) {
           FirebaseMessaging.getInstance().subscribeToTopic("/topics/alramOn");
          //  FirebaseMessaging.getInstance().subscribeToTopic("/topics/alramOnTest");
            SharedPreferencesManager.setStringValue(getApplicationContext(), SharedPreferenceConst.PUSH_STATUS_TEXT, getString(R.string.alarm_on));
        }

    }


    private void setTabView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragmentList = new ArrayList<Fragment>();

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < mFragment.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTitles[i]).setIndicator(getTabView(i));
            mTabHost.addTab(tabSpec, mClass[i], null);
            mFragmentList.add(mFragment[i]);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    private View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        image.setImageResource(mImages[index]);
        title.setText(mTitles[index]);

        return view;
    }

    private void setTabEvent() {

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {

        //get current tab index.
        int index = mTabHost.getCurrentTab();

        //decide what to do
        if(index != 0){
            mTabHost.setCurrentTab(index - 1);
        } else {
            super.onBackPressed();
        }
    }
}
