package com.nguyensao.myapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.nguyensao.myapplication.fagments.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductsActivity extends AppCompatActivity {
    ApiCaller apiCaller;
    JSONArray jsonArrayDataProduct;
    FlexboxLayout flexboxLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ImageView imageFrist = findViewById(R.id.imageFrist);
        imageFrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsActivity.this,
                        MainActivity.class));
                finish();
            }
        });

        flexboxLayout = findViewById(R.id.flexContainer);
        apiCaller=ApiCaller.getInstance(this);
        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                jsonArrayDataProduct = apiCaller.stringToJsonArray(response);
                if (jsonArrayDataProduct != null) {
                    try {
                        for (int i = 0; i < jsonArrayDataProduct.length(); i++) {
                            addItemProduct(jsonArrayDataProduct.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("Error", "Invalid JSON data");
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });



    }
    public void addItemProduct(JSONObject jsonObject) {
        View item = LayoutInflater.from(this).inflate(R.layout.item_product, flexboxLayout, false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        LinearLayout touchLiner = item.findViewById(R.id.itemContanier);
        String imageProduct = "";
        try {
            textName.setText(jsonObject.getString("title"));
            textPrice.setText(String.valueOf(jsonObject.getInt("price")) + "k");
            imageProduct = jsonObject.getString("photo");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        touchLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, DetailActivity.class);
                intent.putExtra("ItemProduct", jsonObject.toString());
                startActivity(intent);
            }
        });

        Picasso.get()
                .load(apiCaller.url + "/image/products/" + imageProduct)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.error)
                .into(imgProduct);
        flexboxLayout.addView(item);
    }

}