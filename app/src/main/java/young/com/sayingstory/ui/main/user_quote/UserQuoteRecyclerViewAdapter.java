package young.com.sayingstory.ui.main.user_quote;


import android.content.Context;
import android.content.Intent;
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
import young.com.sayingstory.ui.constants.QuoteImage;
import young.com.sayingstory.ui.main.detailed_quote.DetailedQuoteActivity;
import young.com.sayingstory.util.ObjectUtil;

public class UserQuoteRecyclerViewAdapter extends RecyclerView.Adapter<UserQuoteRecyclerViewAdapter.ViewHolder> {

    public final String TAG = "QuoteRecyclerAdapter";
    private List<Quote> mQuoteList;
    private Context mContext;
    private int background_img_number;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView quoteText;
        public TextView author;
        public ImageView background;

        public ViewHolder(View view) {
            super(view);
            quoteText = (TextView) view.findViewById(R.id.quote_text);
            author = (TextView) view.findViewById(R.id.author);
            background = (ImageView) view.findViewById(R.id.background_imageview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            Quote quote = mQuoteList.get(getPosition());
            Intent intent = new Intent(mContext, DetailedQuoteActivity.class);
            intent.putExtra("quote", quote);
            intent.putExtra("quoteType", "userQuote");

            mContext.startActivity(intent);
        }
    }

    //construct
    public UserQuoteRecyclerViewAdapter(Context context, List<Quote> mQuoteList) {
        mContext = context;
        this.mQuoteList = mQuoteList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_quote_recyclerview_row, parent, false);
        int height = parent.getMeasuredHeight() / 4;
        itemView.setMinimumHeight(height);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        background_img_number = ObjectUtil.RANDOM.nextInt(QuoteImage.BACKGROUND_IMG_COUNT);
        mQuoteList.get(position).setBackgroundImgNumber(background_img_number);
        Quote userQuote = mQuoteList.get(position);

        Glide.with(mContext)
                .load(QuoteImage.quoteBackgroundDrawable[userQuote.getBackgroundImgNumber()])
                .into(holder.background);
        holder.quoteText.setText(userQuote.getQuoteText());
        holder.author.setText(userQuote.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mQuoteList.size();
    }
}
