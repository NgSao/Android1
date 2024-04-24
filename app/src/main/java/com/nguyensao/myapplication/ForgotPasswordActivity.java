package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText textUser = findViewById(R.id.textUser);
        EditText textPass = findViewById(R.id.textPass);
        EditText textPassAgain = findViewById(R.id.textPassAgain);
        ImageButton btnShowPassword1 = findViewById(R.id.btnShowPassword1);
        ImageButton btnShowPassword2 = findViewById(R.id.btnShowPassword2);
        Button btnSignForgot = findViewById(R.id.btnSignForgot);
        TextView textSignIn = findViewById(R.id.textSignIn);

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

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,SignInActivity.class));
            }
        });


        ApiCaller apiCaller = ApiCaller.getInstance(this);
        btnSignForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = textPass.getText().toString();
                String newPasswordAgain = textPassAgain.getText().toString();
                if (newPassword.length() < 8 || !isValidPassword(newPassword)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có ít nhất 8 ký tự, bao gồm ít nhất một chữ thường, một chữ hoa, một số và một ký tự đặc biệt.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(newPasswordAgain)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu nhập lại không khớp với mật khẩu mới.", Toast.LENGTH_SHORT).show();
                    return;
                }
                apiCaller.makeStringRequest(apiCaller.url + "/users/name/" + textUser.getText().toString(), new ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        User newUser = new User(
                                textUser.getText().toString(),
                                newPassword,
                                "null",
                                "null",
                                "null");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            User.setId(jsonObject.getInt("id"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        apiCaller.updateUser(newUser, new ApiCaller.ApiResponseListener<User>() {
                            @Override
                            public void onSuccess(User response) {
                                Toast.makeText(getApplicationContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(getApplicationContext(), "Không thể khôi phục mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private void togglePasswordVisibility(EditText editText) {
        int selection = editText.getSelectionEnd(); // Lưu vị trí con trỏ
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        editText.setSelection(selection); // Khôi phục vị trí con trỏ
    }
    private boolean isValidPassword(String password) {
        return password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
}