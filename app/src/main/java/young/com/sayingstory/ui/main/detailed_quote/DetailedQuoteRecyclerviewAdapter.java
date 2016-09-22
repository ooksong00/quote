package young.com.sayingstory.ui.main.detailed_quote;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;
import young.com.sayingstory.R;
import young.com.sayingstory.data.model.Quote;
import young.com.sayingstory.data.model.QuoteComment;
import young.com.sayingstory.ui.constants.QuoteImage;
import young.com.sayingstory.util.ObjectUtil;

public class DetailedQuoteRecyclerviewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final String TAG = "DetailedQuoteAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM= 1;
    private static final int ZERO_COMMNET = 0;
    private Context mContext;
    private List<QuoteComment> mQuoteCommentList;
    private Quote mQuote;
    private String mQuoteType;

    public DetailedQuoteRecyclerviewAdapter(Context context, Quote quote, List<QuoteComment> quoteCommentList, String quoteType) {
        mContext = context;
        mQuote = quote;
        mQuoteCommentList = quoteCommentList;
        mQuoteType = quoteType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_HEADER == viewType && mQuoteType.equals("quote")) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detailed_quote_header_recyclerview_row, parent, false);

            return new ViewHeaderHolder(itemView);
        } else if(TYPE_HEADER == viewType && mQuoteType.equals("userQuote")) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detailed_user_quote_header_recyclerview_row, parent, false);
            return new ViewHeaderHolder(itemView);
        }
        else  if(TYPE_ITEM == viewType){
            View itemView2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.detailed_quote_items_recyclerview_row, parent, false);

            return new ViewItemHolder(itemView2);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHeaderHolder) {  //Header
            ViewHeaderHolder viewHeaderHolder = (ViewHeaderHolder) holder;

            Glide.with(mContext)
                    .load(QuoteImage.quoteBackgroundDrawable[mQuote.getBackgroundImgNumber()])
                    .into(viewHeaderHolder.quoteBackgroundImg);

            viewHeaderHolder.createdAt.setText(mQuote.getCreatedAt());
            viewHeaderHolder.author.setText(mQuote.getAuthor());
            viewHeaderHolder.quoteText.setText(mQuote.getQuoteText());

            //Is visible comment text
            if(mQuoteCommentList.size() > ZERO_COMMNET) {
                viewHeaderHolder.comments.setVisibility(View.VISIBLE);
                viewHeaderHolder.suggestionComment.setVisibility(View.INVISIBLE);
            }

            Log.d(TAG, "ViewHeaderHolder");
        } else if(holder instanceof  ViewItemHolder) { //Items
            ViewItemHolder viewItemHolder = (ViewItemHolder) holder;
            QuoteComment quoteComment = mQuoteCommentList.get(position - 1);

            Glide.with(mContext)
                    .load(QuoteImage.getCommentUserDrawable())
                    .into(((ViewItemHolder) holder).userImg);

            viewItemHolder.comment.setText(quoteComment.getComment());
            viewItemHolder.createdAt.setText(quoteComment.getCreatedAt());


            viewItemHolder.userId.setText(quoteComment.getUserId());
            Log.d(TAG, "ViewItemHolder");
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mQuoteCommentList.size() + 1; //because of header, add + 1
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder{
        TextView comment;
        TextView createdAt;
        TextView userId;
        ImageView userImg;


        public ViewItemHolder(View view) {
            super(view);
            comment = (TextView) view.findViewById(R.id.comment);
            createdAt = (TextView ) view.findViewById(R.id.created_at);
            userId = (TextView) view.findViewById(R.id.user_id);
            userImg = (ImageView) view.findViewById(R.id.user_img);

        }
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder {
        ImageView transparetType;
        TextView createdAt;
        TextView author;
        TextView quoteText;
        ImageView quoteBackgroundImg;
        TextView comments;
        TextView suggestionComment;

        public ViewHeaderHolder(View view) {
            super(view);
            createdAt = (TextView) view.findViewById(R.id.created_at);
            author = (TextView) view.findViewById(R.id.author);
            quoteText = (TextView) view.findViewById(R.id.quote_text);
            quoteBackgroundImg = (ImageView) view.findViewById(R.id.quote_background_img);
            comments = (TextView) view.findViewById(R.id.comments_text);
            suggestionComment = (TextView) view.findViewById(R.id.suggestion_comment);
            transparetType = (ImageView) view.findViewById(R.id.transparent_type);
        }
    }
}
