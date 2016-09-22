package young.com.sayingstory.data.response;

import java.util.List;

import young.com.sayingstory.data.model.QuoteComment;

public class QuoteCommentsResponse {

    public Data data;

    public class Data {
        public List<QuoteComment> quoteComments;
    }
}
