package young.com.sayingstory.data.remote;

import retrofit2.http.GET;
import rx.Observable;
import young.com.sayingstory.data.response.QuoteResponse;


public interface QuoteApi {

    @GET("english_quote/quote/getQuotes")
    Observable<QuoteResponse> getQuote();

}
