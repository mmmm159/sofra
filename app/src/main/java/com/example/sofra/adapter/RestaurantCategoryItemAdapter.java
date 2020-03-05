package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantCategoryItemAdapter extends RecyclerView.Adapter<RestaurantCategoryItemAdapter.CategoryItemViewHolder> {


    private Context context;
    private List<ItemRestaurantData> categoryItemDataList;

    public RestaurantCategoryItemAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ItemRestaurantData> categoryItemDataList) {
        this.categoryItemDataList = categoryItemDataList;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_category_item
                , parent, false);
        return new CategoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {

        ItemRestaurantData categoryItemData = categoryItemDataList.get(position);

        String name = categoryItemData.getName();
        String descrption = categoryItemData.getDescription();
        String imgUrl = categoryItemData.getPhotoUrl();
        String price = categoryItemData.getPrice();

        holder.itemRestaurantCategoryItemTxtViewCatName.setText(name);
        holder.itemRestaurantCategoryItemTxtViewCatDescription.setText(descrption);
        holder.itemRestaurantCategoryItemTxtViewPriceValue.setText(price);
        Glide.with(context).load(imgUrl).into(holder.itemRestaurantCategoryItemImg);

    }

    @Override
    public int getItemCount() {
        return categoryItemDataList.size();
    }



    class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_restaurant_category_item_btn_delete)
        ImageButton itemRestaurantCategoryItemBtnDelete;
        @BindView(R.id.item_restaurant_category_item_img_btn_edit)
        ImageButton itemRestaurantCategoryItemImgBtnEdit;
        @BindView(R.id.item_restaurant_category_item_img)
        ImageView itemRestaurantCategoryItemImg;
        @BindView(R.id.item_restaurant_category_item_txt_view_cat_name)
        TextView itemRestaurantCategoryItemTxtViewCatName;
        @BindView(R.id.item_restaurant_category_item_txt_view_cat_description)
        TextView itemRestaurantCategoryItemTxtViewCatDescription;
        @BindView(R.id.item_restaurant_category_item_txt_view_price_value)
        TextView itemRestaurantCategoryItemTxtViewPriceValue;
        @BindView(R.id.item_restaurant_category_item_constraint_container)
        ConstraintLayout itemRestaurantCategoryItemConstraintContainer;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.item_restaurant_category_item_btn_delete, R.id.item_restaurant_category_item_img_btn_edit, R.id.item_restaurant_category_item_constraint_container})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.item_restaurant_category_item_btn_delete:
                    break;
                case R.id.item_restaurant_category_item_img_btn_edit:

                    break;
                case R.id.item_restaurant_category_item_constraint_container:
                    break;
            }
        }
    }
}
