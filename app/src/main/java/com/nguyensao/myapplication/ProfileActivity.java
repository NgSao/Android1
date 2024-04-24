package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uriImg=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        View bottomSheetView2 = getLayoutInflater().inflate(R.layout.img_layout, null);
        final BottomSheetDialog bottomSheetDialog;
        bottomSheetDialog = new BottomSheetDialog(ProfileActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView2);

        TextView photo=bottomSheetView2.findViewById(R.id.camera);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });
        TextView upimage = bottomSheetView2.findViewById(R.id.hinh);
        upimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Chọn ảnh "),PICK_IMAGE_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null&& data.getData()!=null) {
            uriImg=data.getData();
            View bottomSheetView = getLayoutInflater().inflate(R.layout.img_layout, null);
            @SuppressLint("WrongViewCast") CircleImageView imageview=findViewById(R.id.imgAvatar);

            imageview.setImageURI(uriImg);
        }
    }
}