package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckOutActivity extends AppCompatActivity {
    EditText editTextName, editTextAddress, editTextPhoneNumber;
    Button btnOrder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
//        ImageView imageFrist = findViewById(R.id.imageFrist);
//        imageFrist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        // Nhận dữ liệu từ Intent
//        Intent intent = getIntent();
//        String receivedData = intent.getStringExtra("ListItemCart");
//        String quantity = intent.getStringExtra("quantity");
//        String priceTotal = intent.getStringExtra("priceTotal");
//        String deliveryCharge = intent.getStringExtra("deliveryCharge");
//        String discount = intent.getStringExtra("discount");
//        int priceTotalNumber = getIntent().getIntExtra("priceTotalNumber", 0);
//
//        TextView txtQuantity = findViewById(R.id.textQuantity);
//        TextView txtPriceTotal = findViewById(R.id.txtSumTotal);
//        TextView txtDeliveryCharge = findViewById(R.id.txtDelivery);
//        TextView txtDiscount = findViewById(R.id.txtDiscount);
//        TextView txtPrice = findViewById(R.id.priceTotal);
//
//
//        txtPrice.setText(String.valueOf(priceTotalNumber));
//        txtQuantity.setText(quantity);
//        txtPriceTotal.setText(priceTotal);
//        txtDeliveryCharge.setText(deliveryCharge);
//        txtDiscount.setText(discount);
//        editTextName = findViewById(R.id.editTextName);
//        editTextAddress = findViewById(R.id.editTextAddress);
//        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
//        btnOrder = findViewById(R.id.btnOrder);
//        btnOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = editTextName.getText().toString();
//                String address = editTextAddress.getText().toString();
//                String phoneNumber = editTextPhoneNumber.getText().toString();
//                // Kiểm tra điều kiện không được bỏ trống tên, địa chỉ và số điện thoại
//                if (name.isEmpty()) {
//                    editTextName.setError("Name cannot be blank");
//                    return;
//                }
//                if (address.isEmpty()) {
//                    editTextAddress.setError("Address cannot be left blank");
//                    return;
//                }
//                if (phoneNumber.isEmpty()) {
//                    editTextPhoneNumber.setError("Phone number can not be left blank");
//                    return;
//                }
//                if (address.length() < 10) {
//                    editTextAddress.setError("The address must be at least 10 characters");
//                    return;
//                }
//
//                // Chuyển dữ liệu đơn hàng sang OrderActivity
//                Intent intent = new Intent(CheckOutActivity.this, CheckOut2Activity.class);
//                intent.putExtra("ListItemCart", receivedData);
//                intent.putExtra("quantity", quantity);
//                intent.putExtra("priceTotal", priceTotal);
//                intent.putExtra("deliveryCharge", deliveryCharge);
//                intent.putExtra("discount", discount);
//                intent.putExtra("priceTotalNumber", priceTotalNumber);
//                intent.putExtra("name", name);
//                intent.putExtra("address", address);
//                intent.putExtra("phoneNumber", phoneNumber);
//                startActivity(intent);
//            }
//        });



    }
}