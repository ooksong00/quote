package young.com.sayingstory.ui.main.user_writing_quote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import young.com.sayingstory.R;
import young.com.sayingstory.data.remote.UserQuoteApi;
import young.com.sayingstory.ui.constants.SharedPreferenceConst;
import young.com.sayingstory.ui.event.LoginEvent;

import young.com.sayingstory.ui.event.UserQuoteWritingEvent;
import young.com.sayingstory.util.LimitedEditText;
import young.com.sayingstory.util.NetworkModule;
import young.com.sayingstory.util.SharedPreferencesManager;

public class UserWritingQuoteActivity extends AppCompatActivity {

    private LimitedEditText userQuoteEdittext;
    private final String TAG = "UserWritingQuote";
    private TextView userIdTextView;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_writing_quote);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.post_menu));

        setUserIdTextView();
        setUserQuoteEditText();

    }
    protected class CustomInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start,
                                   int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[\\s\"-_/?><,.~`'!@#$%^&*()-_+=|a-zA-Z0-9]+$");

            if(source.equals("") || ps.matcher(source).matches()){
                return source;
            }

            Toast.makeText(getApplicationContext(),
                    getString(R.string.restriction_hangul), Toast.LENGTH_SHORT).show();

            return "";
        }
    }
    private void setUserIdTextView() {
        String userId = SharedPreferencesManager.getStringValue(this, SharedPreferenceConst.USER_NAME);
        userIdTextView = (TextView) findViewById(R.id.user_id);
        userIdTextView.setText(userId);
    }

    private void setUserQuoteEditText() {
        userQuoteEdittext = (LimitedEditText) findViewById(R.id.user_writing_edittext);
        userQuoteEdittext.setMaxLines(10);
        userQuoteEdittext.setMaxCharacters(280);

        // 영문자와 숫자만 입력할 수 있도록 필터링.
        userQuoteEdittext.setFilters(new InputFilter[]{new CustomInputFilter()});

        // 포커스가 주어졌을 때 보여지는 키보드의 타입을 영어로 설정.
        userQuoteEdittext.setPrivateImeOptions("defaultInputmode=english;");

    }


    private void insertUserQuote() {
        String userId = SharedPreferencesManager.getStringValue(this, SharedPreferenceConst.USER_NAME);

        UserQuoteApi userQuoteApi = NetworkModule.createService(UserQuoteApi.class);
        Observable<Response<ResponseBody>> insertUserQuote = userQuoteApi
                .insertUserQuote(userId, userQuoteEdittext.getText().toString());
        mSubscriptions.add(insertUserQuote
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ResponseBody>>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new UserQuoteWritingEvent(getString(R.string.wrote)));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_writing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Log.d(TAG,"onOptionItemSelected");
        switch (menuItem.getItemId()) {
            case R.id.menu_post:
                insertUserQuote();
                onBackPressed();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
