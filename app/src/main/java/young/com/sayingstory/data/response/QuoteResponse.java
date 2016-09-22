package young.com.sayingstory.data.response;

import java.util.List;

import young.com.sayingstory.data.model.Quote;

public class QuoteResponse {

    public Data data;

    public class Data {
        public List<Quote> quotes;
    }
}
