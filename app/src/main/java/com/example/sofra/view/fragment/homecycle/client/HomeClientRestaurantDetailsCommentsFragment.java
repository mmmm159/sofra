package com.example.sofra.view.fragment.homecycle.client;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sofra.R;
import com.example.sofra.adapter.RestaurantCommentsAdapter;
import com.example.sofra.data.api.ApiService;
import com.example.sofra.data.api.RetrofitClient;
import com.example.sofra.data.model.client.review.Review;
import com.example.sofra.data.model.client.review.ReviewData;
import com.example.sofra.data.model.client.reviewnopagination.ReviewNoPagination;
import com.example.sofra.utils.OnEndLess;
import com.example.sofra.utils.Utils;
import com.example.sofra.view.fragment.BaseFragment;
import com.example.sofra.view.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeClientRestaurantDetailsCommentsFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.fragment_home_client_restaurant_details_comments_btn_add_comment)
    Button fragmentHomeClientRestaurantDetailsCommentsBtnAddComment;
    @BindView(R.id.fragment_home_client_restaurant_details_comments_recycler)
    RecyclerView fragmentHomeClientRestaurantDetailsCommentsRecycler;
    @BindView(R.id.fragment_home_client_restaurant_details_comments_progress_bar)
    ProgressBar fragmentHomeClientRestaurantDetailsCommentsProgressBar;
    @BindView(R.id.fragment_home_client_restaurant_details_comments_txt_view_network_error)
    TextView fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError;


    private ApiService apiService;
    private List<ReviewData> reviewDataList = new ArrayList<>();
    private RestaurantCommentsAdapter restaurantCommentsAdapter;
    private EditText commentEdtTxt;
    private RadioButton emojiAngry;
    private RadioButton emojiSad;
    private RadioButton emojiSmile;
    private RadioButton emojiLaugh;
    private RadioButton emojiLove;
    private ConstraintLayout constraintMain;
    private TextView onResponseSuccessTxtView;
    private ProgressBar progressBar;

    private int rate;
    private TextView onResponseErrorTxtView;


    public HomeClientRestaurantDetailsCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initFragment();
        View view = inflater.inflate(R.layout.fragment_home_client_restaurant_details_comments, container, false);
        ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        loadComments();
        return view;
    }

    private void loadComments() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseActivity);
        fragmentHomeClientRestaurantDetailsCommentsRecycler.setLayoutManager(linearLayoutManager);
        OnEndLess onEndLess = new OnEndLess(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {
                getPages(current_page);
            }
        };
        fragmentHomeClientRestaurantDetailsCommentsRecycler.addOnScrollListener(onEndLess);
        restaurantCommentsAdapter = new RestaurantCommentsAdapter(baseActivity, reviewDataList);
        fragmentHomeClientRestaurantDetailsCommentsRecycler.setAdapter(restaurantCommentsAdapter);

        Utils.showProgressBar(fragmentHomeClientRestaurantDetailsCommentsRecycler,
                fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError
                , fragmentHomeClientRestaurantDetailsCommentsProgressBar);
        getPages(1);


    }

    private void getPages(int page) {
        apiService.getRestaurantsComments("HRbqKFSaq5ZpsOKITYoztpFZNylmzL9elnlAThxZSZ52QWqVBIj8Rdq7RhoB"
                , 2,page).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        if (response.body().getData().getData().size()==0&&page==1){

                            fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError.setText(
                                    baseActivity.getString(R.string.default_response_no_comments_yet)
                            );

                            Utils.showErrorText(fragmentHomeClientRestaurantDetailsCommentsRecycler,
                                    fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError,
                                    fragmentHomeClientRestaurantDetailsCommentsProgressBar);

                        }
                        else {
                            Utils.showContainer(fragmentHomeClientRestaurantDetailsCommentsRecycler,
                                    fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError,
                                    fragmentHomeClientRestaurantDetailsCommentsProgressBar);
                            reviewDataList.addAll(response.body().getData().getData());
                            restaurantCommentsAdapter.notifyDataSetChanged();



                        }


                    } else {

                        fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError.setText(response.body().getMsg());
                        Utils.showErrorText(fragmentHomeClientRestaurantDetailsCommentsRecycler,
                                fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError,
                                fragmentHomeClientRestaurantDetailsCommentsProgressBar);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {

                fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError.setText(
                        baseActivity.getString(R.string.default_response_no_internet_connection));
                Utils.showErrorText(fragmentHomeClientRestaurantDetailsCommentsRecycler,
                        fragmentHomeClientRestaurantDetailsCommentsTxtViewNetworkError,
                        fragmentHomeClientRestaurantDetailsCommentsProgressBar);
            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }


    @OnClick(R.id.fragment_home_client_restaurant_details_comments_btn_add_comment)
    public void onViewClicked() {

        Dialog dialog = new Dialog(baseActivity);
        dialog.setContentView(R.layout.dialog_rate_restaurant);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        HomeActivity homeActivity = (HomeActivity) baseActivity;
        homeActivity.getActivityBottomNav().setVisibility(View.GONE);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                homeActivity.getActivityBottomNav().setVisibility(View.VISIBLE);

            }
        });
        dialog.setCancelable(true);

        commentEdtTxt = dialog.findViewById(R.id.dialog_rate_restaurant_edt_txt_add_comment);

        constraintMain = dialog.findViewById(R.id.dialog_rate_restaurant_constraint_main_content);
        onResponseSuccessTxtView = dialog.findViewById(R.id.dialog_rate_restaurant_txt_view_default_error);
        progressBar = dialog.findViewById(R.id.dialog_rate_restaurant_progressbar);
        emojiAngry = dialog.findViewById(R.id.dialog_rate_restaurant_emoji_angry);
        emojiSad = dialog.findViewById(R.id.dialog_rate_restaurant_emoji_sad);
        emojiSmile = dialog.findViewById(R.id.dialog_rate_restaurant_emoji_smile);
        emojiLaugh = dialog.findViewById(R.id.dialog_rate_restaurant_emoji_laugh);
        emojiLove = dialog.findViewById(R.id.dialog_rate_restaurant_emoji_love);
        onResponseErrorTxtView = dialog.findViewById(R.id.dialog_rate_restaurant_txt_view_on_response_0);
        Button button = dialog.findViewById(R.id.dialog_rate_restaurant_btn_add);

        button.setOnClickListener(this);
        emojiAngry.setOnClickListener(this);
        emojiSad.setOnClickListener(this);
        emojiSmile.setOnClickListener(this);
        emojiLaugh.setOnClickListener(this);
        emojiLove.setOnClickListener(this);


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.dialog_rate_restaurant_emoji_angry:
                rate = 1;
                break;
            case R.id.dialog_rate_restaurant_emoji_sad:
                rate = 2;
                break;
            case R.id.dialog_rate_restaurant_emoji_smile:
                rate = 3;
                break;
            case R.id.dialog_rate_restaurant_emoji_laugh:
                rate = 4;
                break;
            case R.id.dialog_rate_restaurant_emoji_love:
                rate = 5;
                break;
            case R.id.dialog_rate_restaurant_btn_add:
                String comment = commentEdtTxt.getText().toString();
                Utils.showProgressBar(constraintMain, null, progressBar);

                apiService.addReview(rate, comment, 2, "HRbqKFSaq5ZpsOKITYoztpFZNylmzL9elnlAThxZSZ52QWqVBIj8Rdq7RhoB")
                        .enqueue(new Callback<ReviewNoPagination>() {
                            @Override
                            public void onResponse(Call<ReviewNoPagination> call, Response<ReviewNoPagination> response) {
                                try {
                                    if (response.body().getStatus() == 1) {

                                        String msg = response.body().getMsg();
                                        onResponseSuccessTxtView.setText(msg + response.body().getData().getReview().getRate());
                                        Utils.showErrorText(constraintMain, onResponseSuccessTxtView, progressBar);
                                        rate = 0;

                                    } else {
                                        Utils.showContainer(constraintMain, onResponseSuccessTxtView, progressBar);
                                        String msg = response.body().getMsg();
                                        onResponseErrorTxtView.setText("!! " + msg + " !!");
                                        onResponseErrorTxtView.setVisibility(View.VISIBLE);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReviewNoPagination> call, Throwable t) {

                                Log.d("testFragment", "onFailure: " + t.getMessage());

                                String msg = baseActivity.getString(R.string.default_response_no_internet_connection);
                                onResponseSuccessTxtView.setText(msg);
                                Utils.showErrorText(constraintMain, onResponseSuccessTxtView, progressBar);

                            }
                        });


                break;
        }
    }
}
