package com.nguyensao.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        TextView textSignUp = findViewById(R.id.textSignUp);
        EditText textUser = findViewById(R.id.textEmail);
        EditText textPass = findViewById(R.id.textPass);
        ImageButton btnShowPassword1 = findViewById(R.id.btnShowPassword1);

        CheckBox checkboxRemember = findViewById(R.id.remember);
        TextView textForgotPassword=findViewById(R.id.textForgotPassword);


        LinearLayout facebookButton = findViewById(R.id.LinerFacebook); // Giả sử bạn đã có một id cho ImageView của bạn
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở ứng dụng Facebook
                Intent intent;
                try {
                    // Kiểm tra xem ứng dụng Facebook đã được cài đặt chưa
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://")); // Đây là URI scheme để mở ứng dụng Facebook
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (PackageManager.NameNotFoundException e) {
                    // Nếu ứng dụng Facebook chưa được cài đặt, bạn có thể chuyển người dùng đến trang Facebook trên trình duyệt web
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/")); // Đây là URL cho trang web Facebook
                }
                startActivity(intent);
            }
        });

        LinearLayout googleButton = findViewById(R.id.LinearGogolel); // Giả sử bạn đã có một id cho ImageView của bạn
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở ứng dụng Google
                Intent intent;
                try {
                    // Kiểm tra xem ứng dụng Google đã được cài đặt chưa
                    getPackageManager().getPackageInfo("com.google.android.googlequicksearchbox", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")); // Đây là URI scheme để mở ứng dụng Google
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (PackageManager.NameNotFoundException e) {
                    // Nếu ứng dụng Google chưa được cài đặt, bạn có thể chuyển người dùng đến trang Google trên trình duyệt web
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")); // Đây là URL cho trang web Google
                }
                startActivity(intent);
            }
        });




        ApiCaller apiCaller = ApiCaller.getInstance(this);

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
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));
            }
        });

        checkboxRemember.setChecked(getCheckboxRemember(SignInActivity.this));
        if (checkboxRemember.isChecked()) {
            textUser.setText(getUsername(SignInActivity.this));
            textPass.setText(getPassword(SignInActivity.this));
        }
        if (getUsername(SignInActivity.this).equals("")) {
            checkboxRemember.setChecked(false);
            textUser.setText("");
            textPass.setText("");
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textUser.getText().toString().trim();
                String password = textPass.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên người dùng và mật khẩu", Toast.LENGTH_LONG).show();
                    return;
                }

                apiCaller.makeStringRequest(apiCaller.url + "/users/name/" + textUser.getText().toString(), new
                        ApiCaller.ApiResponseListener<String>() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    User.setId(jsonObject.getInt("id"));
                                    if (BCrypt.checkpw(textPass.getText().toString(), jsonObject.getString("pass"))) {
                                        User.setNamewelcome(jsonObject.getString("username"));
                                        User.setEmailUser(jsonObject.getString("email"));
                                        User.setPhoneUser(jsonObject.getString("numphone"));
                                        User.setPassUser(jsonObject.getString("pass"));
                                        User.setAdressUser(jsonObject.getString("photo"));
                                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                        if (checkboxRemember.isChecked()) {
                                            setUser(SignInActivity.this, textUser.getText().toString().trim(),
                                                    textPass.getText().toString().trim(), checkboxRemember.isChecked());
                                        } else {
                                            setUser(SignInActivity.this, "", "", checkboxRemember.isChecked());
                                        }
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        intent.putExtra("userObject", response);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sai mật khẩu!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(getApplicationContext(), "Người dùng không tồn tại!" + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
    }

    public  void  setUser(Context context, String username, String password, boolean rememberMe) {
        SharedPreferences prefs=context.getSharedPreferences("myUserPackage",0);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putBoolean("remember", rememberMe);
        editor.commit();
    }
    public String getUsername(Context context) {
        SharedPreferences prefs=context.getSharedPreferences("myUserPackage",0);
        return  prefs.getString("username","");
    }
    public String getPassword(Context context) {
        SharedPreferences prefs=context.getSharedPreferences("myUserPackage",0);
        return  prefs.getString("password","");
    }
    public boolean getCheckboxRemember(Context context) {
        SharedPreferences prefs=context.getSharedPreferences("myUserPackage",0);
        return  prefs.getBoolean("remember",false);
    }
    private void togglePasswordVisibility(EditText editText) {
        int selection = editText.getSelectionEnd(); // Save cursor position
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        editText.setSelection(selection); // Restore cursor position
    }

}



