package young.com.sayingstory.data.response;

import java.util.List;

import young.com.sayingstory.data.model.UserQuote;

public class UserQuoteResponse {

    public Data data;

    public class Data {
        public List<UserQuote> userQuotes;
    }
}
