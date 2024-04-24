package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nguyensao.myapplication.fagments.CartFragment;
import com.nguyensao.myapplication.fagments.HomeFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class CheckOut2Activity extends AppCompatActivity {
    TextView imgTime;
    int hour, minute;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out2);
        imgTime = findViewById(R.id.imgTime);
        calendar = Calendar.getInstance();

        // Lấy giờ và phút hiện tại
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Hiển thị giờ và phút hiện tại trên TextView imgTime
        updateLabel();

        ImageView imageFrist = findViewById(R.id.imageFrist);
        imageFrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String receivedData = intent.getStringExtra("ListItemCart");
        String quantity = intent.getStringExtra("quantity");
        String priceTotal = intent.getStringExtra("priceTotal");
        String deliveryCharge = intent.getStringExtra("deliveryCharge");
        String discount = intent.getStringExtra("discount");
        int priceTotalNumber = getIntent().getIntExtra("priceTotalNumber", 0);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String address = intent.getStringExtra("address");

        // Hiển thị dữ liệu trên giao diện
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtAddress = findViewById(R.id.txtAddress);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtphoneNumber = findViewById(R.id.txtphoneNumber);
        TextView txtQuantity = findViewById(R.id.textQuantity);
        TextView txtPriceTotal = findViewById(R.id.txtSumTotal);
        TextView txtDeliveryCharge = findViewById(R.id.txtDelivery);
        TextView txtDiscount = findViewById(R.id.txtDiscount);
        TextView txtPrice = findViewById(R.id.priceTotal);

        // Hiển thị giá trị lên giao diện
        txtPrice.setText(String.valueOf(priceTotalNumber));
        txtQuantity.setText(quantity);
        txtPriceTotal.setText(priceTotal);
        txtDeliveryCharge.setText(deliveryCharge);
        txtDiscount.setText(discount);
        txtAddress.setText(address);
        txtphoneNumber.setText(phoneNumber);


        ImageView mapIcon = findViewById(R.id.mapIcon);
//        mapIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CheckOut2Activity.this, MapsActivity.class);
//                startActivity(intent);
//            }
//        });
        mapIcon.setOnClickListener(v -> openGoogleMapsWithCurrentLocation());


        Button btnCheckOut = findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển dữ liệu đơn hàng sang OrderActivity
                Intent intent = new Intent(CheckOut2Activity.this, OrderActivity.class);
                intent.putExtra("ListItemCart", receivedData);
                intent.putExtra("quantity", quantity);
                intent.putExtra("priceTotal", priceTotal);
                intent.putExtra("deliveryCharge", deliveryCharge);
                intent.putExtra("discount", discount);
                intent.putExtra("priceTotalNumber", priceTotalNumber);
                startActivity(intent);
            }
        });


    }


    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Thiết lập giờ và phút
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                // Hiển thị thời gian đã chọn trên TextView imgTime
                updateLabel();
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Set Time");
        timePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "hh:mm a, EEEE dd/MM/yyyy"; // Định dạng hiển thị
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        imgTime.setText(sdf.format(calendar.getTime())); // Hiển thị thời gian trên TextView
    }

        private void openGoogleMapsWithCurrentLocation() {
        // Mở Google Maps với vị trí hiện tại
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q="));
        startActivity(intent);
    }




}