package com.example.sofra.adapter.client;

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
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ItemViewHolder> {



    private Context context;
    private List<ItemRestaurantData> foodItemsList;
    private int position;

    public FoodItemsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ItemRestaurantData> foodItemsList) {

        this.foodItemsList = foodItemsList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_home_client_restaurant_food
                , parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        this.position = position;

        ItemRestaurantData foodItemData = foodItemsList.get(position);
        String imgUrl = foodItemData.getPhotoUrl();
        String foodName = foodItemData.getName();
        String description = foodItemData.getDescription();
        String priceValue = foodItemData.getPrice();
        if (foodItemData.getHasOffer()) {
            String offerPrice = foodItemData.getOfferPrice();
            holder.itemHomeClientRestaurantFoodLinePriceValue.setVisibility(View.VISIBLE);
            holder.itemHomeClientRestaurantFoodTxtViewNewPriceValue.setText(offerPrice);
            holder.itemHomeClientRestaurantFoodTxtViewNewPriceValue.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(imgUrl).into(holder.itemHomeClientRestaurantFoodImg);
        holder.itemHomeClientRestaurantFoodTxtViewFoodName.setText(foodName);
        holder.itemHomeClientRestaurantFoodTxtViewDescription.setText(description);
        holder.itemHomeClientRestaurantFoodTxtViewPriceValue.setText(priceValue);



    }


    @Override
    public int getItemCount() {
        return foodItemsList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.item_home_client_restaurant_food_img)
        ImageView itemHomeClientRestaurantFoodImg;
        @BindView(R.id.item_home_client_restaurant_food_txt_view_description)
        TextView itemHomeClientRestaurantFoodTxtViewDescription;
        @BindView(R.id.item_home_client_restaurant_food_txt_view_price_value)
        TextView itemHomeClientRestaurantFoodTxtViewPriceValue;
        @BindView(R.id.item_home_client_restaurant_food_line_price_value)
        View itemHomeClientRestaurantFoodLinePriceValue;
        @BindView(R.id.item_home_client_restaurant_food_txt_view_new_price_value)
        TextView itemHomeClientRestaurantFoodTxtViewNewPriceValue;
        @BindView(R.id.item_home_client_restaurant_food_txt_view_food_name)
        TextView itemHomeClientRestaurantFoodTxtViewFoodName;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.item_home_client_restaurant_food_img, R.id.item_home_client_restaurant_food_card_view_img_container, R.id.item_home_client_restaurant_food_constraint_txt_container, R.id.item_home_client_restaurant_food_card_view_txt_container})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.item_home_client_restaurant_food_img:
                    break;
                case R.id.item_home_client_restaurant_food_card_view_img_container:
                    break;
                case R.id.item_home_client_restaurant_food_constraint_txt_container:
                    break;
                case R.id.item_home_client_restaurant_food_card_view_txt_container:
                    break;
            }
        }


    }
}
