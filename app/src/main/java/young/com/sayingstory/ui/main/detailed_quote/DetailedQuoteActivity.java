package young.com.sayingstory.ui.main.detailed_quote;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import young.com.sayingstory.R;
import young.com.sayingstory.data.model.Quote;
import young.com.sayingstory.data.remote.DetailedQuoteApi;
import young.com.sayingstory.data.response.QuoteCommentsResponse;
import young.com.sayingstory.ui.constants.SharedPreferenceConst;
import young.com.sayingstory.ui.main.main_screen.MainActivity;
import young.com.sayingstory.ui.main.user_quote.UserQuoteFragment;
import young.com.sayingstory.ui.setting.login.LoginDialogActivity;
import young.com.sayingstory.util.NetworkModule;
import young.com.sayingstory.util.RxUtil;
import young.com.sayingstory.util.SharedPreferencesManager;

public class DetailedQuoteActivity extends AppCompatActivity {

    private static final String TAG = "DetailedQuoteActivity";
    private RecyclerView mDetailedQuoteRecyclerview;
    private DetailedQuoteRecyclerviewAdapter mDetailedQuoteRecyclerviewAdapter;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private Quote quote;
    private InputMethodManager inputMethodManager;
    private EditText editText;
    private ProgressBar progressBar;
    private String quoteType;
    private String push;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_quote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.quote));
        ButterKnife.bind(this);
        editText = (EditText) findViewById(R.id.edit_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //get
        quote = (Quote) getIntent().getExtras().getSerializable("quote");
        quoteType = getIntent().getExtras().getString("quoteType"); //quote or userQuote args
        push = getIntent().getExtras().getString("push"); //Only receive it when coming from fcm.

        //loading comments
        if(quoteType.equals("quote")){
            setQuoteCommentsByQuoteId();
        } else if(quoteType.equals("userQuote")) {
            setUserQuoteCommentsByQuoteId();
        }


    }

    private void setUserQuoteCommentsByQuoteId() {
        DetailedQuoteApi detailedQuoteApi = NetworkModule.createService(DetailedQuoteApi.class);
        Observable<QuoteCommentsResponse> getUserQuoteCommentsById = detailedQuoteApi
                .getUserQuoteCommentsById(quote.getId());
        mSubscriptions.add(getUserQuoteCommentsById
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QuoteCommentsResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(QuoteCommentsResponse quoteCommentsResponse) {
                        mDetailedQuoteRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_detailed_quote);
                        mDetailedQuoteRecyclerviewAdapter = new DetailedQuoteRecyclerviewAdapter(getApplicationContext(),
                                quote, quoteCommentsResponse.data.quoteComments, quoteType);
                        mDetailedQuoteRecyclerviewAdapter.notifyDataSetChanged();
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mDetailedQuoteRecyclerview.setLayoutManager(mLayoutManager);
                        mDetailedQuoteRecyclerview.setAdapter(mDetailedQuoteRecyclerviewAdapter);
                    }
                })
        );
    }

    //Loading comments from server and set recyclerview
    public void setQuoteCommentsByQuoteId() {
        DetailedQuoteApi detailedQuoteApi = NetworkModule.createService(DetailedQuoteApi.class);
        Observable<QuoteCommentsResponse> getQuoteCommentsById = detailedQuoteApi
                .getQuoteCommentsById(quote.getId());
        mSubscriptions.add(getQuoteCommentsById
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QuoteCommentsResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(QuoteCommentsResponse quoteCommentsResponse) {
                        mDetailedQuoteRecyclerview = (RecyclerView) findViewById(R.id.recyclerview_detailed_quote);
                        mDetailedQuoteRecyclerviewAdapter = new DetailedQuoteRecyclerviewAdapter(getApplicationContext(),
                                quote, quoteCommentsResponse.data.quoteComments, quoteType);
                        mDetailedQuoteRecyclerviewAdapter.notifyDataSetChanged();
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mDetailedQuoteRecyclerview.setLayoutManager(mLayoutManager);
                        mDetailedQuoteRecyclerview.setAdapter(mDetailedQuoteRecyclerviewAdapter);
                    }
                })
        );

    }
    @OnClick(R.id.comment_button)
    public void onClickCommentButton(){
        String userId = SharedPreferencesManager.getStringValue(this, SharedPreferenceConst.USER_NAME);

        //checking id auth
        if(userId == null) {
            startActivity(new Intent(this, LoginDialogActivity.class));
            return;
        }

        if(!(editText.getText().toString().length() == 0)) {
            //After input something, check id auth
            if(userId == null) {
                startActivity(new Intent(this, LoginDialogActivity.class));
                return;
            }
            Log.d(TAG, "onClickCommentButton");
            if(quoteType.equals("quote")) {
                insertQuoteComment(editText);
            } else if(quoteType.equals("userQuote")) {
                insertUserQuoteComment(editText);
            }

        }

    }

    private void insertUserQuoteComment(final EditText editText) {
        String userId = SharedPreferencesManager.getStringValue(this, SharedPreferenceConst.USER_NAME);

        DetailedQuoteApi detailedQuoteApi = NetworkModule.createService(DetailedQuoteApi.class);
        Observable<Response<ResponseBody>> insertUserQuoteComment = detailedQuoteApi
                .insertUserQuoteComment(quote.getId(), userId, editText.getText().toString());
        mSubscriptions.add(insertUserQuoteComment
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        setUserQuoteCommentsByQuoteId(); //After writing comments by user, it update comments
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        editText.setText(""); //delete comment because of needing its reset.
                    }

                    @Override
                    public void onError(Throwable e) {
                        FirebaseCrash.report(e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        Log.d(TAG, "Status Code = " + responseBodyResponse.toString());
                    }
                })
        );
    }



    private void insertQuoteComment(final EditText editText) {
        String userId = SharedPreferencesManager.getStringValue(this, SharedPreferenceConst.USER_NAME);

        DetailedQuoteApi detailedQuoteApi = NetworkModule.createService(DetailedQuoteApi.class);
        Observable<Response<ResponseBody>> insertQuoteComment = detailedQuoteApi
                .insertQuoteComment(quote.getId(), userId, editText.getText().toString());
        mSubscriptions.add(insertQuoteComment
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        setQuoteCommentsByQuoteId(); //After writing comments by user, it update comments
                        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        editText.setText(""); //delete comment because of needing its reset.
                    }

                    @Override
                    public void onError(Throwable e) {
                        FirebaseCrash.report(e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        Log.d(TAG, "Status Code = " + responseBodyResponse.toString());
                    }
                })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.unSubscriptionsSubscribe(mSubscriptions);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        if(push != null) { //Fcm send push string
            push = null;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return false;
        } else { //Fcm not send it
            onBackPressed();
            return super.onSupportNavigateUp();
        }

    }

    @Override
    public void onBackPressed() {
        if(push != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
           finish();
        }
    }
}
