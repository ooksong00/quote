package young.com.sayingstory.ui.main.user_quote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import young.com.sayingstory.R;
import young.com.sayingstory.data.remote.UserQuoteApi;
import young.com.sayingstory.data.response.QuoteResponse;
import young.com.sayingstory.ui.constants.SharedPreferenceConst;
import young.com.sayingstory.ui.event.LoginEvent;
import young.com.sayingstory.ui.event.UserQuoteWritingEvent;
import young.com.sayingstory.ui.main.user_writing_quote.UserWritingQuoteActivity;
import young.com.sayingstory.ui.setting.login.LoginDialogActivity;
import young.com.sayingstory.util.NetworkModule;
import young.com.sayingstory.util.RxUtil;
import young.com.sayingstory.util.SharedPreferencesManager;

public class UserQuoteFragment extends Fragment {
    private final String TAG = "UserQuoteFragment";

    private View mRootView;
    private RecyclerView mUserQuoteRecyclerview;
    private Subscription mSubscription;
    private UserQuoteRecyclerViewAdapter mUserQuoteRecyclerviewAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.user_quote_fragment,container,false);

        setVisibleProgressbar();
        setUserQuoteRecyclerview();
        setFloatingActionButton();

        return mRootView;
    }

    private void setFloatingActionButton() {
        fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = SharedPreferencesManager.getStringValue(getActivity(), SharedPreferenceConst.USER_NAME);

                //checking id auth
                if(userId == null) {
                    startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                    return;
                }

                Intent intent = new Intent(getActivity(), UserWritingQuoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setVisibleProgressbar() {
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setUserQuoteRecyclerview() {
        mUserQuoteRecyclerview = (RecyclerView) mRootView.findViewById(R.id.recyclerview_quote);
        mUserQuoteRecyclerview.setHasFixedSize(true);
        UserQuoteApi userQuoteApi = NetworkModule.createService(UserQuoteApi.class);
        Observable<QuoteResponse> call = userQuoteApi.getUserQuotes();
        mSubscription = call
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QuoteResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        FirebaseCrash.report(e);
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onNext(QuoteResponse userQuoteResponse) {
                        mUserQuoteRecyclerviewAdapter = new UserQuoteRecyclerViewAdapter(getActivity(), userQuoteResponse.data.quotes);
                        mUserQuoteRecyclerviewAdapter.notifyDataSetChanged();
                        layoutManager = new GridLayoutManager(getActivity(), 2);
                        mUserQuoteRecyclerview.setLayoutManager(layoutManager);

                        mUserQuoteRecyclerview.setAdapter(mUserQuoteRecyclerviewAdapter);

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserQuoteWritingEvent event) {
        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
        setUserQuoteRecyclerview();
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
    public void onDestroy() {
        super.onDestroy();
        RxUtil.unsubscribe(mSubscription);
    }

}
