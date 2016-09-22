package young.com.sayingstory;

import com.facebook.stetho.Stetho;

public class EnglishQuoteApplication extends android.app.Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

    }

}