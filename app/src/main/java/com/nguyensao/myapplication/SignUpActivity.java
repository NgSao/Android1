package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class SignUpActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uriImg = null;
    ApiCaller apiCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView textSignIn = findViewById(R.id.textSignIn);
        EditText textUser = findViewById(R.id.textUser);
        EditText textPhone = findViewById(R.id.textPhone);
        EditText textEmail = findViewById(R.id.textEmail);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText textAdress= findViewById(R.id.textAdress);
        EditText textPass = findViewById(R.id.textPass);
        EditText textPassAgain = findViewById(R.id.textPassAgain);
        ImageButton btnShowPassword1 = findViewById(R.id.btnShowPassword1);
        ImageButton btnShowPassword2 = findViewById(R.id.btnShowPassword2);

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        textPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnShowPassword1.setVisibility(View.VISIBLE);
                    btnShowPassword1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            togglePasswordVisibility(textPass);
                        }
                    });
                } else {
                    // Nếu không, ẩn ImageButton
                    btnShowPassword1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textPassAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnShowPassword2.setVisibility(View.VISIBLE);
                    btnShowPassword2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            togglePasswordVisibility(textPassAgain);
                        }
                    });
                } else {
                    // Nếu không, ẩn ImageButton
                    btnShowPassword2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });






        apiCaller = ApiCaller.getInstance(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textUser.getText().toString().trim();
                String password = textPass.getText().toString().trim();
                String passwordAgain = textPassAgain.getText().toString().trim();
                String email = textEmail.getText().toString().trim();
                String phone = textPhone.getText().toString().trim();
                String address = textAdress.getText().toString().trim();


                if (username.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidUsername(username)) {
                    Toast.makeText(getApplicationContext(), "Tên người dùng không hợp lệ", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 8 ký tự, bao gồm ít nhất một chữ thường, một chữ hoa, một số và một ký tự đặc biệt.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordAgain.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                    return;
                }

                // Kiểm tra email hợp lệ
                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Địa chỉ email không hợp lệ", Toast.LENGTH_LONG).show();
                }
                else {

                    User newUser = new User(
                            username,
                            password,
                            address,
                            phone,
                            email
                    );
                    apiCaller.addUser(newUser, new ApiCaller.ApiResponseListener<User>() {
                        @Override
                        public void onSuccess(User response) {
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(getApplicationContext(), "Lỗi! Mã lỗi của bạn " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }


    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidUsername(String username) {
        return username.length() >= 3 && username.matches("[a-zA-Z0-9]+");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
    private void togglePasswordVisibility(EditText editText) {
        int selection = editText.getSelectionEnd();
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        editText.setSelection(selection);
    }
}