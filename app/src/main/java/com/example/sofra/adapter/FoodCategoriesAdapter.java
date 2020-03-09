package com.example.sofra.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.homecycle.client.HomeClientRestaurantDetailsMenuFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FoodCategoriesAdapter extends RecyclerView.Adapter<FoodCategoriesAdapter.CategoryViewHolder> {


    private AppCompatActivity context;
    private List<ItemRestaurantData> categoriesList;
    private int position;

    public FoodCategoriesAdapter(AppCompatActivity context) {
        this.context = context;
    }

    public void setData(List<ItemRestaurantData> categoriesList) {

        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_home_client_restaurant_category
                , parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        this.position = position;

        ItemRestaurantData itemRestaurantData = categoriesList.get(position);
        String imgUrl = itemRestaurantData.getPhotoUrl();
        String text = itemRestaurantData.getName();

        Glide.with(context).load(imgUrl).into(holder.itemHomeClientRestaurantCategoryImg);
        holder.itemHomeClientRestaurantCategoryTxt.setText(text);

    }


    @Override
    public int getItemCount() {
        return categoriesList.size();
    }



    class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_client_restaurant_category_img)
        CircleImageView itemHomeClientRestaurantCategoryImg;
        @BindView(R.id.item_home_client_restaurant_category_txt)
        TextView itemHomeClientRestaurantCategoryTxt;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.item_home_client_restaurant_category_img, R.id.item_home_client_restaurant_category_txt})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.item_home_client_restaurant_category_img:
                    getItemsById();
                    break;
                case R.id.item_home_client_restaurant_category_txt:
                    getItemsById();
                    break;
            }
        }


        private void getItemsById() {
            int catId = categoriesList.get(getAdapterPosition()).getId();
            HomeClientRestaurantDetailsMenuFragment fragment = new
                    HomeClientRestaurantDetailsMenuFragment();
            fragment.categoryId = catId;
            Utils.replaceFragment(context.getSupportFragmentManager(),
                    R.id.activity_home_frame,fragment);
        }
    }


}
