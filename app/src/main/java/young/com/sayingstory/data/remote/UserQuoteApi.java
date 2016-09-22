package young.com.sayingstory.data.remote;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import young.com.sayingstory.data.response.QuoteResponse;
import young.com.sayingstory.data.response.UserQuoteResponse;

public interface UserQuoteApi {

    @FormUrlEncoded
    @POST("english_quote/userQuote/insertUserQuote")
    rx.Observable<Response<ResponseBody>> insertUserQuote(
            @Field("user_id") String userId,
            @Field("quote_text") String quoteText);

    @GET("english_quote/userQuote/getUserQuotes")
    Observable<QuoteResponse> getUserQuotes();
}
