package com.example.sofra.adapter.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.data.model.client.review.ReviewData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantCommentsAdapter extends RecyclerView.Adapter<RestaurantCommentsAdapter.CommentViewHolder> {


    private Context context;
    private List<ReviewData> reviewDataList;

    public RestaurantCommentsAdapter(Context context, List<ReviewData> reviewDataList) {
        this.context = context;
        this.reviewDataList = reviewDataList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_home_client_restaurant_comment
                , parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        ReviewData reviewData = reviewDataList.get(position);
        String userName = reviewData.getClient().getName();
        String comment = reviewData.getComment();
        String rate = reviewData.getRate();
        switch (rate){
            case "1":
                holder.itemHomeClientRestaurantCommentEmoji.setImageResource(R.drawable.ic_emoji_angry_magenta);
                break;
            case "2":
                holder.itemHomeClientRestaurantCommentEmoji.setImageResource(R.drawable.ic_emoji_sad_magenta);
                break;
            case "3":
                holder.itemHomeClientRestaurantCommentEmoji.setImageResource(R.drawable.ic_emoji_smile_magenta);
                break;
            case "4":
                holder.itemHomeClientRestaurantCommentEmoji.setImageResource(R.drawable.ic_emoji_laugh_magenta);
                break;
            case "5":
                holder.itemHomeClientRestaurantCommentEmoji.setImageResource(R.drawable.ic_emoji_love_magenta);
                break;
        }

        holder.itemHomeClientRestaurantCommentTxtViewName.setText(userName);
        holder.itemHomeClientRestaurantCommentTxtViewCommentText.setText(comment);

    }

    @Override
    public int getItemCount() {
        return reviewDataList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_client_restaurant_comment_emoji)
        ImageView itemHomeClientRestaurantCommentEmoji;
        @BindView(R.id.item_home_client_restaurant_comment_txt_view_name)
        TextView itemHomeClientRestaurantCommentTxtViewName;
        @BindView(R.id.item_home_client_restaurant_comment_txt_view_comment_text)
        TextView itemHomeClientRestaurantCommentTxtViewCommentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
