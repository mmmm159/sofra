package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.model.general.auth.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private Context context;
    private List<User> restaurantDataList;

    public RestaurantListAdapter(Context context , List<User> restaurantDataList) {
        this.context = context;
        this.restaurantDataList = restaurantDataList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_home_client, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        User restaurant = restaurantDataList.get(position);
        String imgUrl = restaurant.getPhotoUrl();
        String resName = restaurant.getName();
        String resStatus = restaurant.getAvailability();
        Double rate = restaurant.getRate();
        String minCharge = restaurant.getMinimumCharger();
        String deliverCost = restaurant.getDeliveryCost();

        Glide.with(context).load(imgUrl).into(holder.itemRecyclerHomeClientImg);
        holder.itemRecyclerHomeClientTxtViewResName.setText(resName);
        setupAvailability(resStatus,holder);
        setRateStars(rate,holder);
        holder.itemRecyclerHomeClientTxtViewMinChargeValue.setText(minCharge);
        holder.itemRecyclerHomeClientTxtViewDeliveryCostValue.setText(deliverCost);

    }

    private void setRateStars(double rate, RestaurantViewHolder holder) {
        int intRate = (int)rate;
        int starsArrayLength = holder.getRateStars().length;

        switch (intRate){

            case 0:

                for (int i = 0; i <starsArrayLength ; i++)
                    holder.getRateStars()[i].setImageResource(R.drawable.ic_star_border_black_24dp);

                break;

            case 1:
                for (int i = 0; i <starsArrayLength-(starsArrayLength-1) ; i++)
                    holder.getRateStars()[i].setImageResource(R.drawable.ic_star_border_light_magenta_24dp);

                break;
            case 2:

                for (int i = 0; i <starsArrayLength-(starsArrayLength-2) ; i++)
                    holder.getRateStars()[i].setImageResource(R.drawable.ic_star_border_light_magenta_24dp);

                break;

            case 3:

                for (int i = 0; i <starsArrayLength-(starsArrayLength-3) ; i++)
                    holder.getRateStars()[i].setImageResource(R.drawable.ic_star_border_light_magenta_24dp);

                break;
            case 4:

                for (int i = 0; i <starsArrayLength ; i++)
                    holder.getRateStars()[i].setImageResource(R.drawable.ic_star_border_light_magenta_24dp);

                break;
        }
    }


    private void setupAvailability(String resStatus, RestaurantViewHolder holder) {
        if (resStatus!=null) {
            if (resStatus.equals("open")){
                holder.itemRecyclerHomeClientTxtViewResStatus.setText(context.getString(
                        R.string.item_recycler_home_client_txt_view_res_status_open));
                holder.itemRecyclerHomeClientTxtViewResStatus
                        .setTextColor(context.getResources().getColor(R.color.green_res_status));
                holder.itemRecyclerHomeClientCircleResStatus.setImageResource(R.drawable.shape_circle_green);
            }
            else {

                holder.itemRecyclerHomeClientTxtViewResStatus.setText(context.getString(
                        R.string.item_recycler_home_client_txt_view_res_status_closed));
                holder.itemRecyclerHomeClientTxtViewResStatus
                        .setTextColor(context.getResources().getColor(R.color.grey));
                holder.itemRecyclerHomeClientCircleResStatus.setImageResource(R.drawable.shape_circle_grey);
            }
        }
    }

    @Override
    public int getItemCount() {
        return restaurantDataList.size();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recycler_home_client_txt_view_res_name)
        TextView itemRecyclerHomeClientTxtViewResName;
        @BindView(R.id.item_recycler_home_client_star_1)
        ImageView itemRecyclerHomeClientStar1;
        @BindView(R.id.item_recycler_home_client_star_2)
        ImageView itemRecyclerHomeClientStar2;
        @BindView(R.id.item_recycler_home_client_star_3)
        ImageView itemRecyclerHomeClientStar3;
        @BindView(R.id.item_recycler_home_client_star_4)
        ImageView itemRecyclerHomeClientStar4;
        @BindView(R.id.item_recycler_home_client_txt_view_min_charge_value)
        TextView itemRecyclerHomeClientTxtViewMinChargeValue;
        @BindView(R.id.item_recycler_home_client_txt_view_delivery_cost_value)
        TextView itemRecyclerHomeClientTxtViewDeliveryCostValue;
        @BindView(R.id.item_recycler_home_client_circle_res_status)
        ImageView itemRecyclerHomeClientCircleResStatus;
        @BindView(R.id.item_recycler_home_client_txt_view_res_status)
        TextView itemRecyclerHomeClientTxtViewResStatus;
        @BindView(R.id.item_recycler_home_client_img)
        CircleImageView itemRecyclerHomeClientImg;

         ImageView[] rateStars = new ImageView[4];

        private ImageView[] getRateStars() {
            rateStars[0] = itemRecyclerHomeClientStar1;
            rateStars[1] = itemRecyclerHomeClientStar2;
            rateStars[2] = itemRecyclerHomeClientStar3;
            rateStars[3] = itemRecyclerHomeClientStar4;
            return rateStars;
        }

        private RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
