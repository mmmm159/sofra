package com.example.sofra.data.api;

import com.example.sofra.data.model.general.auth.Auth;
import com.example.sofra.data.model.general.category.Category;
import com.example.sofra.data.model.restaurant.categorywithpagination.CategoryWithPagination;
import com.example.sofra.data.model.general.itemrestaurant.ItemRestaurant;
import com.example.sofra.data.model.general.onerestaurant.OneRestaurant;
import com.example.sofra.data.model.general.region.Region;
import com.example.sofra.data.model.general.reset.Reset;
import com.example.sofra.data.model.general.restaurant.Restaurant;
import com.example.sofra.data.model.general.review.Review;
import com.example.sofra.data.model.general.reviewnopagination.ReviewNoPagination;
import com.example.sofra.data.model.restaurant.editcategory.EditCategory;
import com.example.sofra.data.model.restaurant.newcategory.NewCategory;
import com.example.sofra.data.model.restaurant.newitem.NewItem;
import com.example.sofra.data.model.restaurant.updateitem.UpdateItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("restaurant/sign-up")
    @Multipart
    Call<Auth> register(@Part("name")RequestBody name,
                        @Part("email")RequestBody email,
                        @Part("password") RequestBody password,
                        @Part("password_confirmation") RequestBody passwordConfirmation,
                        @Part("phone") RequestBody phone,
                        @Part("whatsapp") RequestBody whatsApp,
                        @Part("region_id") RequestBody regionId,
                        @Part("delivery_cost") RequestBody deliveryCost,
                        @Part("minimum_charger") RequestBody minimumCharger,
                        @Part MultipartBody.Part file,
                        @Part("delivery_time") RequestBody deliveryTime);

    @GET("cities")
    Call<Region> getCities();

    @GET("regions")
    Call<Region> getRegions(@Query("city_id") int cityId);

    @POST("client/login")
    @FormUrlEncoded
    Call<Auth> loginClient(@Field("email") String email,
                                @Field("password") String password);


    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Auth> loginRestaurant(@Field("email") String email,
                                    @Field("password") String password);



    @POST("restaurant/reset-password")
    @FormUrlEncoded
    Call<Reset> resetPasswordRestaurant(@Field("email") String email);

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<Reset> resetPasswordClient(@Field("email") String email);

    @POST("restaurant/new-password")
    @FormUrlEncoded
    Call<Reset> newPasswordRestaurant(@Field("code") String code
    ,@Field("password") String password
    ,@Field("password_confirmation") String passwordConfirmation);


    @POST("client/new-password")
    @FormUrlEncoded
    Call<Reset> newPasswordClient(@Field("code") String code
            ,@Field("password") String password
            ,@Field("password_confirmation") String passwordConfirmation);


    @GET("restaurants")
    Call<Restaurant> getRestaurants(@Query("page") int page);



    @GET("restaurants")
    Call<Restaurant> getRestaurantsFilter(@Query("keyword") String keyword ,@Query("region_id") int regionId );



    @GET("restaurant/reviews")
    Call<Review> getRestaurantsComments(@Query("api_token") String apiToken,
                                       @Query("restaurant_id") int restaurantId,
                                        @Query("page") int page);



    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<ReviewNoPagination> addReview(@Field("rate") int rate
            , @Field("comment") String comment
            , @Field("restaurant_id") int restaurantId
    , @Field("api_token") String apiToken);


    @GET("restaurant")
    Call<OneRestaurant> getRestaurantInfo(@Query("restaurant_id") int restaurantId);


    @GET("categories")
    Call<Category> getCategories(@Query("restaurant_id") int restaurantId);



    @GET("items")
    Call<ItemRestaurant> getRestuarantItems(@Query("restaurant_id") int restaurantId,
                                            @Query("category_id") int categoryId,
                                            @Query("page") int page);




    @GET("restaurant/my-categories")
    Call<CategoryWithPagination> getRestaurantCategories (@Query("api_token") String apiToken,
                                                    @Query("page") int page);


    @POST("restaurant/new-category")
    @Multipart
    Call<NewCategory> addNewCategory(@Part("name")RequestBody name,
                                     @Part MultipartBody.Part file,
                               @Part("api_token")RequestBody apiToken);



    @POST("restaurant/update-category")
    @Multipart
    Call<EditCategory> updateCategory(@Part("name")RequestBody name,
                                      @Part MultipartBody.Part file,
                                      @Part("api_token")RequestBody apiToken,
                                      @Part("category_id")RequestBody categoryId);


    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<EditCategory> deleteCategory(@Field("api_token")String apiToken,@Field("category_id") int catId);


    @POST("restaurant/new-item")
    @Multipart
    Call<NewItem> addNewItem(@Part("description")RequestBody description,
                             @Part("price")RequestBody price,
                             @Part("name")RequestBody name,
                             @Part MultipartBody.Part file,
                             @Part("api_token")RequestBody apiToken,
                             @Part("offer_price")RequestBody offerPrice,
                             @Part("category_id")RequestBody categoryId);


    @GET("restaurant/my-items")
    Call<ItemRestaurant> getRestaurantCategoryItems(@Query("api_token") String apiToken,
                                            @Query("category_id") int categoryId,
                                            @Query("page") int page);




    @POST("restaurant/update-item")
    @Multipart
    Call<UpdateItem> updateItem(@Part("description")RequestBody description,
                                @Part("price")RequestBody price,
                                @Part("name")RequestBody name,
                                @Part MultipartBody.Part file,
                                @Part("item_id") RequestBody itemId,
                                @Part("api_token")RequestBody apiToken,
                                @Part("offer_price")RequestBody offerPrice,
                                @Part("category_id")RequestBody categoryId);


    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<UpdateItem> deleteItem(@Field("item_id") int itemId , @Field("api_token") String apiToken);




}
