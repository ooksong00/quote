package young.com.sayingstory.ui.main.quote;

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

public class QuoteRecyclerViewAdapter extends RecyclerView.Adapter<QuoteRecyclerViewAdapter.ViewHolder> {

    public final String TAG = "QuoteRecyclerAdapter";
    private List<Quote> mQuoteList;
    private Context mContext;
    private int background_img_number;

    //viewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView quoteText;
        public TextView author;
        public TextView createdAt;
        public ImageView background;

        public ViewHolder(View view) {
            super(view);
            quoteText = (TextView) view.findViewById(R.id.quote_text);
            author = (TextView) view.findViewById(R.id.author);
            createdAt = (TextView) view.findViewById(R.id.created_at);
            background = (ImageView) view.findViewById(R.id.background_imageview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick");
            Quote quote = mQuoteList.get(getPosition());
            Intent intent = new Intent(mContext, DetailedQuoteActivity.class);
            intent.putExtra("quote", quote);
            intent.putExtra("quoteType", "quote");
            mContext.startActivity(intent);
        }
    }

    //construct
    public QuoteRecyclerViewAdapter(Context context, List<Quote> mQuoteList) {
        mContext = context;
        this.mQuoteList = mQuoteList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quote_recyclerview_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        background_img_number = ObjectUtil.RANDOM.nextInt(QuoteImage.BACKGROUND_IMG_COUNT);
        mQuoteList.get(position).setBackgroundImgNumber(background_img_number);
        Quote quote = mQuoteList.get(position);

        Glide.with(mContext)
                .load(QuoteImage.quoteBackgroundDrawable[quote.getBackgroundImgNumber()])
                .into(holder.background);
        holder.quoteText.setText(quote.getQuoteText());
        holder.author.setText(quote.getAuthor());
        holder.createdAt.setText(quote.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return mQuoteList.size();
    }
}
