package com.nguyensao.myapplication.fagments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyensao.myapplication.ApiCaller;
import com.nguyensao.myapplication.Cart;
import com.nguyensao.myapplication.CheckOut2Activity;
import com.nguyensao.myapplication.HorizontalNumberPicker;
import com.nguyensao.myapplication.R;
import com.nguyensao.myapplication.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout cartContainerGlobal;
    LayoutInflater inflaterGlobal;
    ApiCaller apiCaller;
    View viewContainer;
    JSONArray jsonArrayCart;
    int priceTotalNumber = 0;
    private TextView txtDiscount;
    private TextView txtDelivery;
    private TextView textQuantity;
    private TextView txtSumTotal;
    private TextView priceTotal;
    private JSONArray mJsonArrayCart;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public void onResume() {
        super.onResume();
        apiCaller.makeStringRequest(apiCaller.url + "/cartDetails/cart/" + Cart.getId(), new ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            jsonArrayCart = new JSONArray(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        cartContainerGlobal.removeAllViews();
                        priceTotalNumber = 0;
                        for (int i = 0; i < jsonArrayCart.length(); i++) {
                            try {
                                int idItemCart = jsonArrayCart.getJSONObject(i).getInt("id");
                                Log.d("dataaaa", jsonArrayCart.getJSONObject(i).toString());
                                Log.d("dataaaaId", String.valueOf(idItemCart));
                                JSONObject itemProductData = new JSONObject(jsonArrayCart.getJSONObject(i).getString("product"));
                                int quantity = jsonArrayCart.getJSONObject(i).getInt("quantity");
                                int price = itemProductData.getInt("price");
                                itemProductData.put("quantity", quantity);
                                itemProductData.put("priceTotal", price * quantity);
                                addItemCart(inflaterGlobal, cartContainerGlobal, itemProductData.toString(), i, idItemCart);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        viewContainer = inflater.inflate(R.layout.fragment_cart, container, false);
        inflaterGlobal = inflater;
        cartContainerGlobal = viewContainer.findViewById(R.id.cartContainer);
        apiCaller = ApiCaller.getInstance(getContext());
        Button btnOrder = viewContainer.findViewById(R.id.btnOrder);
        txtDiscount = viewContainer.findViewById(R.id.txtDiscount);
        txtDelivery = viewContainer.findViewById(R.id.txtDelivery);
        textQuantity = viewContainer.findViewById(R.id.textQuantity);
        txtSumTotal = viewContainer.findViewById(R.id.txtSumTotal);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonArrayCart == null || jsonArrayCart.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cart Empty");
                    builder.setMessage("Your cart is empty. Please add items to proceed.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // If cart is not empty, proceed to checkout
//                    Intent intent = new Intent(getActivity(), CartFragment.class);
//                    intent.putExtra("quantity", textQuantity.getText().toString());
//                    intent.putExtra("priceTotal", txtSumTotal.getText().toString());
//                    intent.putExtra("deliveryCharge", txtDelivery.getText().toString());
//                    intent.putExtra("discount", txtDiscount.getText().toString());
//                    intent.putExtra("priceTotalNumber", priceTotalNumber);
//                    intent.putExtra("ListItemCart", jsonArrayCart.toString());
//                    startActivity(intent);
                    showOrderDialog();
                }
            }
        });



        RelativeLayout shareLayout = viewContainer.findViewById(R.id.share);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện logic chia sẻ ở đây
                shareContent();
            }
        });






        return viewContainer;
    }

    public void addItemCart(LayoutInflater inflater, LinearLayout cartContainer, String data, int index, int idItemCart) {
        int priceItem = 0;
        View item = inflater.inflate(R.layout.item_cart, cartContainer, false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        TextView txtToal = item.findViewById(R.id.txtToal);
        TextView txtQuantity = item.findViewById(R.id.txtQuantity);
        ImageView deleteItemCart = item.findViewById(R.id.deleteItemCart);
        TextView priceTotal = viewContainer.findViewById(R.id.priceTotal);
        textQuantity = viewContainer.findViewById(R.id.textQuantity);
        txtDiscount = viewContainer.findViewById(R.id.txtDiscount);
        txtDelivery = viewContainer.findViewById(R.id.txtDelivery);

        HorizontalNumberPicker np_channel_nr = item.findViewById(R.id.horizontalNumberPicker);
        np_channel_nr.setMax(10);
        np_channel_nr.setMin(1);
        np_channel_nr.setOnValueChangeListener(new HorizontalNumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(int newValue) {
                    showMaintenanceAlert(getContext());
                    np_channel_nr.setValue(np_channel_nr.getValue());
            }
        });



        try {
            JSONObject itemProductData = new JSONObject(data);
            textName.setText(itemProductData.getString("title"));
            txtToal.setText(itemProductData.getString("priceTotal"));

            int quantity = itemProductData.optInt("quantity", 0);
            txtQuantity.setText("Số lượng: " + itemProductData.getString("quantity"));

            // Tính tổng số lượng của tất cả sản phẩm trong giỏ hàng

            int totalQuantity = getTotalQuantity();
            textQuantity.setText(String.valueOf(getCartQuantity()));

            priceItem = itemProductData.getInt("priceTotal");
            priceTotalNumber += priceItem;
            priceTotal.setText(String.valueOf(priceTotalNumber));

            if (priceTotalNumber > 1000) {
                txtDiscount.setText("100"); // Giảm 100
            } else {
                txtDiscount.setText("0"); // Không giảm
            }
            if (totalQuantity > 4) {
                txtDelivery.setText("15");
            } else if (totalQuantity > 0 && totalQuantity < 5) {
                txtDelivery.setText("30");
            } else {
                txtDelivery.setText("0");
            }
            int discount = Integer.parseInt(txtDiscount.getText().toString());
            int deliveryCost = Integer.parseInt(txtDelivery.getText().toString());
            int totalSum = priceTotalNumber - discount + deliveryCost;
            txtSumTotal.setText(String.valueOf(totalSum));

            np_channel_nr.setValue(quantity); // Đặt giá trị cho HorizontalNumberPicker

            Picasso.get()
                    .load(apiCaller.url + "/image/products/" + itemProductData.getString("photo")
                    )
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.error)
                    .into(imgProduct);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        int finalPriceItem = priceItem;
        int finalIdItemCart = idItemCart;
        deleteItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(item, cartContainer, priceTotal, index, finalPriceItem, finalIdItemCart);
            }
        });
        cartContainer.addView(item);
    }

    public void removeItem(View item, LinearLayout cartContainer, TextView priceTotal, int index, int priceItem, int idItemCart) {
        Log.d("MyFragment", String.valueOf(index));
        apiCaller.deleteItemCart(idItemCart, new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                cartContainer.removeView(item);
                int quantity = getCartQuantity() - 1;
                updateCartQuantity(quantity);
                int totalQuantity = getTotalQuantity();
                totalQuantity--;
                priceTotalNumber -= priceItem;
                priceTotal.setText(String.valueOf(priceTotalNumber));
                if (priceTotalNumber > 1000) {
                    txtDiscount.setText("100"); // Giảm 100
                } else {
                    txtDiscount.setText("0"); // Không giảm
                }
                // Cập nhật giá trị txtDelivery dựa trên totalQuantity mới
                if (totalQuantity > 4) {
                    txtDelivery.setText("15");
                } else if (totalQuantity > 0 && totalQuantity < 5) {
                    txtDelivery.setText("30");
                } else {
                    txtDelivery.setText("0");
                }
                int discount = Integer.parseInt(txtDiscount.getText().toString());
                int deliveryCost = Integer.parseInt(txtDelivery.getText().toString());
                int totalSum = priceTotalNumber - discount + deliveryCost;
                txtSumTotal.setText(String.valueOf(totalSum));

                apiCaller.makeStringRequest(apiCaller.url + "/cartDetails/cart/" + Cart.getId(), new ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            jsonArrayCart = new JSONArray(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
    }

    private void showConfirmationDialog(View item, LinearLayout cartContainer, TextView priceTotal, int index, int priceItem, int idItemCart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm khỏi đơn hàng?");
        builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(item, cartContainer, priceTotal, index, priceItem, idItemCart);
                    }
                });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int getCartQuantity() {
        int quantity = 0;
        if (jsonArrayCart != null) {
            quantity = jsonArrayCart.length();
        }
        return quantity;
    }
    private void updateCartQuantity(int quantity) {
        textQuantity.setText(String.valueOf(quantity));
    }
    private int getTotalQuantity() {
        int totalQuantity = 0;
        for (int i = 0; i < jsonArrayCart.length(); i++) {
            try {
                int quantity = jsonArrayCart.getJSONObject(i).getInt("quantity");
                totalQuantity += quantity;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return totalQuantity;
    }

    private void showOrderDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_check_out, null);
        dialogBuilder.setView(dialogView);

        // Khai báo và khởi tạo các thành phần giao diện trong dialogView
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextAddress = dialogView.findViewById(R.id.editTextAddress);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.editTextPhoneNumber);

        editTextName.setText(User.getNamewelcome());
        editTextPhoneNumber.setText(User.getPhoneUser());
        editTextAddress.setText(User.getAdressUser());
        Button btnChooseLocation = dialogView.findViewById(R.id.btnChooseLocation);

        editTextAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                btnChooseLocation.setVisibility(View.VISIBLE);
            } else {
                btnChooseLocation.setVisibility(View.GONE);
            }
        });

        btnChooseLocation.setOnClickListener(v -> openGoogleMapsWithCurrentLocation());


        // Thiết lập các thuộc tính và hành động cho Dialog
        dialogBuilder.setTitle("Check Order Information");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Xử lý thông tin khi người dùng nhấn nút OK

                String name = editTextName.getText().toString();
                String address = editTextAddress.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                    // Hiển thị thông báo "Please fill in all fields" nếu một hoặc nhiều trường trống
                    Toast.makeText(getActivity(), "Please fill in all information", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu không có trường nhập trống, tiếp tục thực hiện các thao tác khác
                    // Tạo Intent để chuyển dữ liệu qua màn hình tiếp theo
                    Intent intent = new Intent(getActivity(), CheckOut2Activity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("quantity", textQuantity.getText().toString());
                    intent.putExtra("priceTotal", txtSumTotal.getText().toString());
                    intent.putExtra("deliveryCharge", txtDelivery.getText().toString());
                    intent.putExtra("discount", txtDiscount.getText().toString());
                    intent.putExtra("priceTotalNumber", priceTotalNumber);
                    intent.putExtra("ListItemCart", jsonArrayCart.toString());
                    startActivity(intent);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Xử lý khi người dùng nhấn nút Cancel
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void openGoogleMapsWithCurrentLocation() {
        // Mở Google Maps với vị trí hiện tại
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q="));
        startActivity(intent);
    }

    private void shareContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tiêu đề của nội dung chia sẻ");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nội dung chia sẻ của bạn...");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }



    private void showMaintenanceAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Hệ thống hiện đang được cập nhật. Vui lòng thử lại sau.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}



