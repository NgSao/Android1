package com.nguyensao.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
public class DetailActivity extends AppCompatActivity {
    JSONObject itemProductData;
    ApiCaller apiCaller;
    String receivedData;
    int productPrice = 0;
    int reviewCount = 0;
    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_REVIEW_COUNT = "reviewCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView btnGoBack = findViewById(R.id.btnGoBack);
        HorizontalNumberPicker np_channel_nr = findViewById(R.id.horizontalNumberPicker);
        ImageView imgProduct = findViewById(R.id.imageProduct);
        TextView txtNameProduct = findViewById(R.id.txtNameProduct);
        TextView txtProductDescription = findViewById(R.id.txtDescription);
        TextView txtPriceProduct = findViewById(R.id.txtPriceProduct);
        Button btnAddCart = findViewById(R.id.btnaddcart);
        TextView txtReview=findViewById(R.id.txtReview);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        // Đọc giá trị reviewCount từ SharedPreferences và cập nhật lên TextView
        reviewCount = sharedPreferences.getInt(KEY_REVIEW_COUNT, 0);
        txtReview.setText(String.valueOf(reviewCount) + " review");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RelativeLayout btnFavorite = findViewById(R.id.ReleveFavorite);


        np_channel_nr.setMax(10);
        np_channel_nr.setMin(1);
        apiCaller = ApiCaller.getInstance(getBaseContext());
// Lấy Intent từ Activity
        Intent intent = getIntent();
// Nhận dữ liệu từ Intent
        if (intent != null) {
            try {
                receivedData = intent.getStringExtra("ItemProduct");
                itemProductData = new JSONObject(receivedData);
                productPrice = itemProductData.getInt("price");
                txtPriceProduct.setText(itemProductData.getString("price")+"k");
                txtNameProduct.setText(itemProductData.getString("title"));
                txtProductDescription.setText(itemProductData.getString("description"));
                itemProductData.put("quantity", np_channel_nr.getValue());
                itemProductData.put("priceTotal", productPrice*np_channel_nr.getValue());
                reviewCount++;
                txtReview.setText(String.valueOf(reviewCount) + " review");


                Picasso.get()
                        .load(apiCaller.url+"/image/products/"+itemProductData.getString("photo"))
                        .placeholder(R.drawable.loading2)
                        .error(R.drawable.error)
                        .into(imgProduct);
            }
            catch (Error e){
                Log.d("Error!",e.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        np_channel_nr.setOnValueChangeListener(new HorizontalNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(int newValue) {
                int priceTotal = productPrice * np_channel_nr.getValue();
                try {
                    itemProductData.put("quantity", np_channel_nr.getValue());
                    itemProductData.put("priceTotal", priceTotal);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                txtPriceProduct.setText(String.valueOf(priceTotal)+ "k");
            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(Cart.getId()!=0){
                            try {
                                apiCaller.addCartDetail(Cart.getId(), itemProductData.getInt("id"), itemProductData.getInt("quantity"), new ApiCaller.ApiResponseListener<JSONObject>() {
                                                    @Override
                                                    public void onSuccess(JSONObject response) {
                                                        Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                                        Log.d("aaaaaaccccccc",
                                                                response.toString());
                                                    }
                                                    @Override
                                                    public void onError(String errorMessage) {
                                                    }
                                                });
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {

                            apiCaller.addCart(User.getId(), new
                                    ApiCaller.ApiResponseListener<JSONObject>() {
                                        @Override

                                        public void onSuccess(JSONObject response) {
                                            try {
                                                Cart.setId(response.getInt("id"));
                                                apiCaller.addCartDetail(Cart.getId(), itemProductData.getInt("id"),
                                                        itemProductData.getInt("quantity"), new
                                                                ApiCaller.ApiResponseListener<JSONObject>() {
                                                                    @Override
                                                                    public void
                                                                    onSuccess(JSONObject response) {
                                                                        Log.d("aaaaaaccccccc", response.toString());
                                                                    }
                                                                    @Override
                                                                    public void onError(String errorMessage) {
                                                                    }
                                                                });
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                        @Override
                                        public void onError(String errorMessage)
                                        {
                                        }
                                    });
                        }
                        finish();
                    }
                },1000);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Create a Favorite instance with user ID and product ID
                            Favorite favorite = new Favorite(User.getId(), itemProductData.getInt("id"));

                            // Add the product to favorites
                            apiCaller.addFavorite(favorite.getUserId(), favorite.getProductId(), new ApiCaller.ApiResponseListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
                                    Log.d("Favorite", "Item added to favorites: " + response.toString());
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Handle error if necessary
                                    Log.e("Favorite", "Error adding item to favorites: " + errorMessage);
                                }
                            });

                            finish(); // Finish the activity
                        } catch (JSONException e) {
                            // Log and handle JSON exception
                            Log.e("DetailActivity", "Error parsing JSON: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, 1000); // Delay execution for 1 second
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();

        // Lưu giá trị reviewCount vào SharedPreferences khi Activity bị pause
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_REVIEW_COUNT, reviewCount);
        editor.apply();
    }
}