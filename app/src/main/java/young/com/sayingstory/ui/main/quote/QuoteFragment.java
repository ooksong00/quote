package young.com.sayingstory.ui.main.quote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import young.com.sayingstory.R;
import young.com.sayingstory.data.remote.QuoteApi;
import young.com.sayingstory.data.response.QuoteResponse;
import young.com.sayingstory.util.NetworkModule;
import young.com.sayingstory.util.RxUtil;

public class QuoteFragment extends Fragment {
    public final String TAG = "QuoteFragment";

    private View mRootView;
    private Subscription mSubscription;
    private RecyclerView mQuoteRecyclerView;
    private QuoteRecyclerViewAdapter mQuoteRecyclerViewAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.quote_fragment,container,false);

        //setProgressBarToVisible
        setVisibleProgressbar();

        //setRecyclerview
        setQuotesRecyclerview();

        return mRootView;
    }

    private void setVisibleProgressbar() {
        progressBar = (ProgressBar) mRootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    //loading quotes from server and setting data to quote recyclerview
    public void setQuotesRecyclerview() {
        mQuoteRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview_quote);
        QuoteApi quoteApi = NetworkModule.createService(QuoteApi.class);
        Observable<QuoteResponse> call = quoteApi.getQuote();
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
                               public void onNext(QuoteResponse quoteResponse) {
                                   Log.d(TAG, "Status Code = " + quoteResponse.toString());
                                   mQuoteRecyclerViewAdapter = new QuoteRecyclerViewAdapter(getActivity(), quoteResponse.data.quotes);
                                   RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                   mQuoteRecyclerView.setHasFixedSize(true);
                                   mQuoteRecyclerView.setLayoutManager(mLayoutManager);
                                   mQuoteRecyclerView.setAdapter(mQuoteRecyclerViewAdapter);
                               }
                           }
                );
    }

    @Override
     public void onDestroy() {
        super.onDestroy();
        RxUtil.unsubscribe(mSubscription);
    }
}
