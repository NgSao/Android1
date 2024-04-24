package com.nguyensao.myapplication.fagments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.navigation.NavigationView;
import com.nguyensao.myapplication.ApiCaller;
import com.nguyensao.myapplication.Cart;
import com.nguyensao.myapplication.CheckOutActivity;
import com.nguyensao.myapplication.DetailActivity;
import com.nguyensao.myapplication.IntroActivity;
import com.nguyensao.myapplication.ProductsActivity;
import com.nguyensao.myapplication.R;
import com.nguyensao.myapplication.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
     ApiCaller apiCaller;
     LinearLayout LinerLayoutProduct, LinerLayoutCategory, LinerLayoutSearchLayout, LinerLayoutProductPopular;
     JSONObject jsonObject;
     JSONArray jsonArrayDataProduct, jsonArrayDataCategory, jsonArray;
     FlexboxLayout flexboxLayout;
     SearchView searchView;
     TextView  txtResults , txtFind;

    private String userName;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải!");
        progressDialog.show();
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntiFinished) {

            }

            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();



        ImageView btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị NavigationView
                NavigationView navigationView = view.findViewById(R.id.navigationView);
                DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(navigationView);
            }
        });



        apiCaller=ApiCaller.getInstance(getContext());
        LinerLayoutProduct = view.findViewById(R.id.LinerLayoutProduct);
        LinerLayoutCategory = view.findViewById(R.id.LinerLayoutCategory);
        LinerLayoutSearchLayout = view.findViewById(R.id.LinearSearch);

        LinerLayoutProductPopular=view.findViewById(R.id.LinerLayoutProductPopular);
        LinearLayout categotyAll=view.findViewById(R.id.categoryAll);
        TextView txtWelcome=view.findViewById(R.id.txtWelcome);
        TextView txtSeeProduct=view.findViewById(R.id.txtSeeProduct);
        TextView txtSeeProduct1=view.findViewById(R.id.txtSeeProduct1);

        txtSeeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ProductsActivity.class);
                startActivity(intent);
            }
        });
        txtSeeProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ProductsActivity.class);
                startActivity(intent);
            }
        });


        RelativeLayout notificationLayout = view.findViewById(R.id.thongbao);
        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotificationDialog();
            }
        });



        txtResults = view.findViewById(R.id.txtResults);
        txtFind = view.findViewById(R.id.txtFind);
        searchView=view.findViewById(R.id.searchView);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn nút tìm kiếm (submit)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 0) {
                    // Hiển thị layout tìm kiếm nếu có chữ được nhập vào
                    LinerLayoutSearchLayout.setVisibility(View.VISIBLE);
                } else {
                    // Ẩn layout tìm kiếm nếu không có chữ
                    LinerLayoutSearchLayout.setVisibility(View.GONE);
                }
                txtResults.setText(newText);
                filterList(newText);
                return true;
            }
        });

        flexboxLayout = view.findViewById(R.id.flexContainer);
        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                jsonArrayDataProduct = apiCaller.stringToJsonArray(response);
                if (jsonArrayDataProduct != null) {
                    try {
                        for (int i = 0; i < jsonArrayDataProduct.length(); i++) {
                            addItemProductSearch(view, inflater, flexboxLayout, jsonArrayDataProduct.getJSONObject(i));
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



//---------
        txtWelcome.setText(User.getNamewelcome());
        apiCaller.makeStringRequest(apiCaller.url + "/slideShows", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArrayData=apiCaller.stringToJsonArray(response);
                if(jsonArrayData!=null) {
                    addBanner(view, jsonArrayData);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                jsonArrayDataProduct = apiCaller.stringToJsonArray(response);
                if (jsonArrayDataProduct != null) {
                    try {
                        int productCount = jsonArrayDataProduct.length();
                        // Hiển thị 6 sản phẩm ngẫu nhiên
                        for (int i = 0; i < Math.min(6, productCount); i++) {
                            addItemProduct(view, inflater, LinerLayoutProductPopular, jsonArrayDataProduct.getJSONObject(i));
                        }

                        // Hiển thị tất cả các sản phẩm còn lại
                        for (int i = 6; i < productCount; i++) {
                            addItemProduct(view, inflater, LinerLayoutProduct, jsonArrayDataProduct.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Chuoi JSON khong hop le");
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

        apiCaller.makeStringRequest(apiCaller.url + "/categories", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                 jsonArrayDataCategory=apiCaller.stringToJsonArray(response);
                if(jsonArrayDataCategory!=null) {
                    try {
                        for (int i = 0; i < jsonArrayDataCategory.length(); i++) {
                            addItemCategory(view, inflater, LinerLayoutCategory, jsonArrayDataCategory.getJSONObject(i));
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else  {
                    System.out.println("Chuoi JSON khong hop le");
                }
            }
            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

        categotyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinerLayoutProduct.removeAllViews();
                if (jsonArrayDataProduct != null) {
                    try {
                        for (int i = 0; i < jsonArrayDataProduct.length(); i++) {
                            addItemProduct(v, inflater,LinerLayoutProduct,jsonArrayDataProduct.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Chuỗi JSON không hợp lệ.");
                }
            }
        });

        apiCaller.makeStringRequest(apiCaller.url + "/carts/user/" + User.getId(), new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCart = jsonArray.getJSONObject(i);
                        Cart.setId(jsonObjectCart.getInt("id"));
                        Log.d("cartId: ", String.valueOf(Cart.getId()));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(String errorMessage) {
                System.out.print(errorMessage);
            }
        });


        return view;
    }



    public void addItemProduct(View view, LayoutInflater inflater, LinearLayout linerLayoutProduct, JSONObject jsonObject) {
        View item=inflater.inflate(R.layout.item_product_home,linerLayoutProduct,false);
        TextView textName=item.findViewById(R.id.txtNameProduct);
        TextView textPrice=item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct=item.findViewById(R.id.imageProduct);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout touchLiner=item.findViewById(R.id.itemContanier);
        String imageProduct = "";
        try {
            textName.setText(jsonObject.getString("title").toString());
            textPrice.setText(String.valueOf(jsonObject.getInt("price"))+"k");
            imageProduct=jsonObject.getString("photo").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        touchLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ItemProduct", jsonObject.toString());
                startActivity(intent);
            }
        });

        Picasso.get()
                .load(apiCaller.url+"/image/products/"+imageProduct)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.error)
                .into(imgProduct);
        linerLayoutProduct.addView(item);
    }
    private void addItemCategory(View view, LayoutInflater inflater, LinearLayout linerLayoutCategory, JSONObject jsonObject){
    View item = inflater.inflate(R.layout.item_category_home, linerLayoutCategory, false);
    TextView textName = item.findViewById(R.id.txtNameCategory);
    ImageView imgProduct = item.findViewById(R.id.imageCategory);
    LinearLayout categoryTouch=item.findViewById(R.id.categoryTouch);
    String imageCategory = "";

    try {
        textName.setText( jsonObject.getString("title").toString());
        imageCategory = jsonObject.getString("photo").toString();
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }
    Picasso.get()
            .load(apiCaller.url+"/image/categories/"+imageCategory)
            .placeholder(R.drawable.loading2)
            .error(R.drawable.error)
            .into(imgProduct);
    linerLayoutCategory.addView(item);

    categoryTouch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinerLayoutProduct = view.findViewById(R.id.LinerLayoutProduct);
            LinerLayoutProduct.removeAllViews();
            TextView txtNameCategoryItem = v.findViewById(R.id.txtNameCategory);
            if (jsonArrayDataProduct != null) {
                try {
                    for (int i = 0; i < jsonArrayDataProduct.length(); i++) {
                        JSONObject jsonObjectItemProduct = jsonArrayDataProduct.getJSONObject(i);
                        JSONObject categoryObject = jsonObjectItemProduct.getJSONObject("category");
                        if (txtNameCategoryItem.getText().toString().equals(categoryObject.getString("title"))) {
                            addItemProduct(v,inflater, LinerLayoutProduct, jsonArrayDataProduct.getJSONObject(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Chuỗi JSON không hợp lệ.");
            }
        }
    });

}

    private void addBanner(View view, JSONArray jsonArray) {
        ViewFlipper viewFlipper = view.findViewById(R.id.bannerView);
        for (int i = 0; i < jsonArray.length(); i++) {
            ImageView imageView = new ImageView(getContext());
            try {
                Picasso.get().load(apiCaller.url + "/image/slideShows/" + jsonArray.getJSONObject(i).getString("photo"))
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                // Set scale type after loading image successfully
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            @Override
                            public void onError(Exception e) {
                                // Handle error if needed
                            }
                        });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(1500);
        viewFlipper.setAutoStart(true);
    }


    private void updateResultCount(int count) {
        txtFind.setText(count + " Results Found");
    }

    private void filterList(String text) {
        JSONArray filteredProducts = new JSONArray();
        if (jsonArrayDataProduct != null) {
            try {
                for (int i = 0; i < jsonArrayDataProduct.length(); i++) {
                    JSONObject product = jsonArrayDataProduct.getJSONObject(i);
                    String productName = product.getString("title").toLowerCase();
                    if (productName.startsWith(text.toLowerCase())) {
                        filteredProducts.put(product);
                    }
                }
                displayFilteredProductList(filteredProducts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void displayFilteredProductList(JSONArray filteredProducts) {
        flexboxLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < filteredProducts.length(); i++) {
            try {
                JSONObject product = filteredProducts.getJSONObject(i);
                addItemProductSearch(getView(), inflater, flexboxLayout, product);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        updateResultCount(filteredProducts.length());
    }
    public void addItemProductSearch(View view, LayoutInflater inflater, FlexboxLayout flexboxLayout, JSONObject jsonObject) {
        View item=inflater.inflate(R.layout.item_product,flexboxLayout,false);
        TextView textName=item.findViewById(R.id.txtNameProduct);
        TextView textPrice=item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct=item.findViewById(R.id.imageProduct);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout touchLiner=item.findViewById(R.id.itemContanier);
        String imageProduct = "";
        try {
            textName.setText(jsonObject.getString("title").toString());
            textPrice.setText(String.valueOf(jsonObject.getInt("price"))+"k");
            imageProduct=jsonObject.getString("photo").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        touchLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ItemProduct", jsonObject.toString());
                startActivity(intent);
            }
        });

        Picasso.get()
                .load(apiCaller.url+"/image/products/"+imageProduct)
                .placeholder(R.drawable.loading2)
                .error(R.drawable.error)
                .into(imgProduct);
        flexboxLayout.addView(item);
    }


    private void showNotificationDialog() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // Giờ hiện tại (24 giờ)
        int minute = calendar.get(Calendar.MINUTE); // Phút hiện tại
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // Ngày hiện tại
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng hiện tại (bắt đầu từ 0 nên cần cộng 1)
        int year = calendar.get(Calendar.YEAR); // Năm hiện tại

        // Tạo thông báo với thông tin về thời gian hiện tại
        String message = "Thời gian: " + hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);

        // Thêm thông tin về ngày tháng năm
        message += "\nCông ty chúng tôi tạm sẽ tạm ngừng hoạt động đến hết ngày hôm nay (" + dayOfMonth + "/" + month + "/" + year + ") để bảo trì.";

        // Thêm lời cảm ơn
        message += "\nCảm ơn bạn đã đồng hành cùng chúng tôi!";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Đóng Dialog nếu người dùng nhấn OK
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}