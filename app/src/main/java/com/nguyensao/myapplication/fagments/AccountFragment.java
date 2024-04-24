package com.nguyensao.myapplication.fagments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nguyensao.myapplication.ApiCaller;
import com.nguyensao.myapplication.CheckOut2Activity;
import com.nguyensao.myapplication.CheckOutActivity;
import com.nguyensao.myapplication.ForgotPasswordActivity;
import com.nguyensao.myapplication.IntroActivity;
import com.nguyensao.myapplication.R;
import com.nguyensao.myapplication.SettingActivity;
import com.nguyensao.myapplication.SignInActivity;
import com.nguyensao.myapplication.SignOutActivity;
import com.nguyensao.myapplication.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ApiCaller apiCaller;
    private TextView txtEmail;
    JSONObject jsonObject;


    ImageView imgAvatar;
    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        apiCaller = ApiCaller.getInstance(getActivity());

        txtUsername.setText(User.getNamewelcome());
        txtEmail.setText(User.getEmailUser());
        TextView textSignOut = view.findViewById(R.id.textSignOut);
        textSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignOutActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        LinearLayout LinearProfile = view.findViewById(R.id.LinearProfile);
        final View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_profile, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);


        TextView txtForgot =bottomSheetView.findViewById(R.id.txtForgot);
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        TextView editUsername = bottomSheetView.findViewById(R.id.txtUsername);
        editUsername.setText(User.getNamewelcome());
        EditText editEmail = bottomSheetView.findViewById(R.id.editEmail);
        editEmail.setText(User.getEmailUser());
        EditText editPhone = bottomSheetDialog.findViewById(R.id.editPhone);
        editPhone.setText(User.getPhoneUser());
        EditText editAddress=bottomSheetDialog.findViewById(R.id.editAddress);
        editAddress.setText(User.getAdressUser());

        EditText editPass = bottomSheetView.findViewById(R.id.editPass);
        EditText editPassNew = bottomSheetView.findViewById(R.id.editPassNew);
        EditText editPassAgain = bottomSheetView.findViewById(R.id.editPassAgain);

        ImageButton btnShowPassword1 = bottomSheetView.findViewById(R.id.btnShowPassword1);
        ImageButton btnShowPassword2 = bottomSheetView.findViewById(R.id.btnShowPassword2);
        ImageButton btnShowPassword3 = bottomSheetView.findViewById(R.id.btnShowPassword3);
        Button btnUpdate=bottomSheetView.findViewById(R.id.btnUpdate);

        editPassNew.addTextChangedListener(new TextWatcher() {
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
                            togglePasswordVisibility(editPassNew);
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

        editPassAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnShowPassword3.setVisibility(View.VISIBLE);
                    btnShowPassword3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            togglePasswordVisibility(editPassAgain);
                        }
                    });
                } else {
                    // Nếu không, ẩn ImageButton
                    btnShowPassword3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ApiCaller apiCaller = ApiCaller.getInstance(getContext());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = editUsername.getText().toString();
                String newPassword = editPassNew.getText().toString();
                String newPasswordAgain = editPassAgain.getText().toString();
                String newEmail = editEmail.getText().toString();
                String newPhone = editPhone.getText().toString();
                String newAdress = editAddress.getText().toString();


                apiCaller.makeStringRequest(apiCaller.url + "/users/name/" + Username, new ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            User.setId(jsonObject.getInt("id"));

                            if (BCrypt.checkpw(editPass.getText().toString(), jsonObject.getString("pass"))) {
                                User newUser = new User(
                                        Username,
                                        newPassword,
                                        newAdress,
                                        newPhone,
                                        newEmail);
                                if (newPassword.length() < 8 || !isValidPassword(newPassword)) {
                                    Toast.makeText(getContext(), "Mật khẩu không hơp lệ.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!newPassword.equals(newPasswordAgain)) {
                                    Toast.makeText(getContext(), "Mật khẩu nhập lại không khớp với mật khẩu mới.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                apiCaller.updateUser(newUser, new ApiCaller.ApiResponseListener<User>() {
                                    @Override
                                    public void onSuccess(User response) {
                                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        Toast.makeText(getContext(), "Không thể khôi phục mật khẩu!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




// Set click listener for LinearProfile
        LinearProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the BottomSheetDialog
                bottomSheetDialog.show();
            }
        });

        LinearLayout linearSetting = view.findViewById(R.id.linearSetting);
        linearSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        LinearLayout LinerShare = view.findViewById(R.id.LinerShare);
        LinerShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareText = "Hãy kiểm tra liên kết tuyệt vời này https://www.facebook.com/profile.php?id=100070104164297";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
            }
        });

        LinearLayout LinearContact = view.findViewById(R.id.LinearContact);
        LinearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });

        LinearLayout linearHelp = view.findViewById(R.id.LinerHelp);
        linearHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để gọi điện hoặc nhắn tin với số điện thoại cụ thể
                Uri phoneNumber = Uri.parse("tel:0392445255"); // Thay đổi số điện thoại tùy thuộc vào nhu cầu
                Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNumber);
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, phoneNumber);
                smsIntent.setData(Uri.parse("sms:" + phoneNumber));

                // Tạo hộp thoại chọn hành động gọi điện hoặc nhắn tin
                Intent chooserIntent = Intent.createChooser(callIntent, "Chọn hành động");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {smsIntent});

                // Kiểm tra xem có ứng dụng nào có thể xử lý hành động không trước khi hiển thị hộp thoại
                if (chooserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(chooserIntent);
                }
            }
        });



        return view;
    }

    public void sendEmail(View view) {
        String userEmail = txtEmail.getText().toString(); // Lấy email người dùng từ TextView txtEmail
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:nguyensaovn2@gmail.com")); // Chỉ định địa chỉ email đích
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Chủ đề email"); // Chủ đề email (có thể bỏ qua)
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Nội dung email"); // Nội dung email (có thể bỏ qua)
        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{userEmail}); // Địa chỉ email người gửi (người dùng nhập vào)
        try {
            startActivity(emailIntent); // Mở ứng dụng email để gửi
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Không có ứng dụng email được cài đặt.", Toast.LENGTH_SHORT).show();
        }
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

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidUsername(String username) {
        // Kiểm tra nếu tên người dùng có ít nhất 3 ký tự và không chứa ký tự đặc biệt
        return username.length() >= 3 && username.matches("[a-zA-Z0-9]+");
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=]).{8,}$";
        return password.matches(passwordPattern);
    }





}