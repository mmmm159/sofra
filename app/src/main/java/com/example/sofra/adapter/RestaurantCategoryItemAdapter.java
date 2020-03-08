package com.example.sofra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.local.SharedPreference;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurantData;
import com.example.sofra.data.model.restaurant.updateitem.UpdateItem;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.homecycle.restaurant.HomeRestaurantAddCategoryItemFragment;
import com.example.sofra.view.fragment.homecycle.restaurant.HomeRestaurantCategoryFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantCategoryItemAdapter extends RecyclerView.Adapter<RestaurantCategoryItemAdapter.CategoryItemViewHolder> {


    private Context context;
    private List<ItemRestaurantData> categoryItemDataList;
    private FragmentManager manager;

    public RestaurantCategoryItemAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ItemRestaurantData> categoryItemDataList , FragmentManager manager) {
        this.categoryItemDataList = categoryItemDataList;
        this.manager = manager;
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

        private ApiService apiService;
        private ItemRestaurantData categoryItemData;


        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            apiService = RetrofitClient.getClient().create(ApiService.class);
        }

        @OnClick({R.id.item_restaurant_category_item_btn_delete, R.id.item_restaurant_category_item_img_btn_edit, R.id.item_restaurant_category_item_constraint_container})
        public void onViewClicked(View view) {
            categoryItemData = categoryItemDataList.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.item_restaurant_category_item_btn_delete:
                    Utils.showProgressDialog(context,context.getString(R.string.default_progress_dialog_msg_deleting));
                    apiService.deleteItem(categoryItemData.getId(), SharedPreference.loadString(context
                    ,SharedPreference.API_TOKEN_KEY)).enqueue(new Callback<UpdateItem>() {
                        @Override
                        public void onResponse(Call<UpdateItem> call, Response<UpdateItem> response) {
                            try {
                                if (response.body().getStatus()==1){

                                    Utils.dismissProgressDialog();
                                    Utils.replaceFragment(manager,R.id.activity_home_frame,
                                            new HomeRestaurantCategoryFragment());
                                }
                                else {
                                    Utils.dismissProgressDialog();
                                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Utils.dismissProgressDialog();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateItem> call, Throwable t) {

                            Utils.dismissProgressDialog();
                            Toast.makeText(context,
                                    context.getString(R.string.default_response_no_internet_connection),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case R.id.item_restaurant_category_item_img_btn_edit:

                    HomeRestaurantAddCategoryItemFragment fragment =
                            new HomeRestaurantAddCategoryItemFragment();

                    fragment.itemDescription = categoryItemData.getDescription();
                    fragment.itemName=categoryItemData.getName();
                    fragment.itemId = String.valueOf(categoryItemData.getId());
                    fragment.itemImgurl = categoryItemData.getPhotoUrl();
                    fragment.itemPrice = categoryItemData.getPrice();
                    fragment.itemOfferPrice = categoryItemData.getOfferPrice();
                    fragment.isUpdateItemFragment = true;

                    Utils.replaceFragment(manager,R.id.activity_home_frame,fragment);


                    break;

            }
        }
    }
}
