package young.com.sayingstory.data.remote;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import young.com.sayingstory.data.response.QuoteCommentsResponse;


public interface DetailedQuoteApi {

    @FormUrlEncoded
    @POST("english_quote/detailedQuote/insertQuoteComment")
    rx.Observable<Response<ResponseBody>> insertQuoteComment(
            @Field("english_quote_view_id") int englishQuoteViewId,
            @Field("user_id") String userId,
            @Field("comment") String comment);

    @GET("english_quote/detailedQuote/getQuoteCommentsById/{english_quote_view_id}")
    Observable<QuoteCommentsResponse> getQuoteCommentsById(@Path("english_quote_view_id") int english_quote_view_id);

    @FormUrlEncoded
    @POST("english_quote/detailedQuote/insertUserQuoteComment")
    rx.Observable<Response<ResponseBody>> insertUserQuoteComment(
            @Field("english_user_quote_id") int englishUserQuoteViewId,
            @Field("user_id") String userId,
            @Field("comment") String comment);

    @GET("english_quote/detailedQuote/getUserQuoteCommentsById/{english_user_quote_id}")
    Observable<QuoteCommentsResponse> getUserQuoteCommentsById(@Path("english_user_quote_id") int english_user_quote_id);
}
