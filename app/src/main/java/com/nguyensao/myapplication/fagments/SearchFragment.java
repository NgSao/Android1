package com.nguyensao.myapplication.fagments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nguyensao.myapplication.ApiCaller;
import com.nguyensao.myapplication.Cart;
import com.nguyensao.myapplication.DetailActivity;
import com.nguyensao.myapplication.ProductsActivity;
import com.nguyensao.myapplication.R;
import com.nguyensao.myapplication.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout LinerLayoutOrder;
     ApiCaller apiCaller;
     JSONArray jsonArrayDataOrders;
    private int orderId;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        LinerLayoutOrder = view.findViewById(R.id.LinerLayoutOrder);
        apiCaller=ApiCaller.getInstance(getContext());
        apiCaller.makeStringRequest(apiCaller.url + "/orders", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                jsonArrayDataOrders = apiCaller.stringToJsonArray(response);
                if (jsonArrayDataOrders != null) {
                    try {
                        for (int i = 0; i < jsonArrayDataOrders.length(); i++) {
                                addItemProduct(view, inflater,LinerLayoutOrder,jsonArrayDataOrders.getJSONObject(i));
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

        RelativeLayout shareLayout = view.findViewById(R.id.share1);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent();
            }
        });


    return view;

    }
    public void addItemProduct(View view, LayoutInflater inflater, LinearLayout LinerLayoutOrder, JSONObject jsonObject) {
        View item=inflater.inflate(R.layout.item_orders,LinerLayoutOrder,false);
       TextView txtId=item.findViewById(R.id.txtId);
       TextView txtDate=item.findViewById(R.id.txtDate);
        try {
            txtId.setText(jsonObject.getString("id").toString());
            txtDate.setText(jsonObject.getString("date").toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        LinerLayoutOrder.addView(item);

        LinearLayout LinearOrder = item.findViewById(R.id.LinearOrder);
        LinearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderProcessingDialog();
            }
        });
    }

    private void showOrderProcessingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Đơn hàng đang được xử lý. Vui lòng quay lại sau!.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void shareContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tiêu đề của nội dung chia sẻ");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nội dung chia sẻ của bạn...");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }




}