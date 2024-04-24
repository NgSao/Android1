package com.nguyensao.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Looper;

import java.io.InputStream;
import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {
    private Handler mHandler;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
            mHandler = new Handler(Looper.getMainLooper());
            ImageView imageFrist = findViewById(R.id.imageFrist);
        imageFrist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

            LinearLayout notificationLayout = findViewById(R.id.ThongBao);
            notificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotificationDialog();
                }
            });

        LinearLayout LinearHelp = findViewById(R.id.LinearHelp);
        LinearHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpCenterDialog();
            }

        });

        LinearLayout aboutLayout = findViewById(R.id.Abouts);
        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        LinearLayout languageLayout = findViewById(R.id.languageLayout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageSelectionDialog();
            }
        });
            LinearLayout privacyLayout = findViewById(R.id.Privacy);
            privacyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPrivacyDialog();
                }
            });


            LinearLayout accountLayout = findViewById(R.id.account);
            accountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Hiển thị thông báo yêu cầu người dùng chờ trong giây lát
                    AlertDialog progressDialog = new AlertDialog.Builder(SettingActivity.this)
                            .setMessage("Vui lòng đợi trong giây lát!.")
                            .setCancelable(false)
                            .create();
                    progressDialog.show();

                    // Tạo một luồng mới để giả lập thời gian chờ
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Chờ trong 3 giây (3000 milis)
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // Sau khi chờ xong, hiển thị thông báo về việc hệ thống đang được bảo trì
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    showMaintenanceDialog();
                                }
                            });
                        }
                    }).start();
                }
            });

    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Giới Thiệu về Công Ty")
                .setMessage("Chào mừng bạn đến với SNShop. Chúng tôi chuyên cung cấp các sản phẩm chất lượng cao và dịch vụ tốt nhất cho khách hàng. Hãy khám phá thêm về chúng tôi!\n\n" +
                        "Chính sách giảm giá và vận chuyển:\n" +
                        "• Khi mua dưới 5 sản phẩm, phí vận chuyển là 30k.\n" +
                        "• Khi mua trên 4 sản phẩm, phí vận chuyển giá 15k.\n" +
                        "• Khi mua hóa đơn trên 1.000k, giảm giá 100k.\n\n" +
                        "Chúng tôi rất vui được đồng hành với bạn.")
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void showLanguageSelectionDialog() {
        final CharSequence[] languages = {"Tiếng Việt", "English"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn Ngôn Ngữ");
        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý sự kiện khi người dùng chọn ngôn ngữ
                switch (which) {
                    case 0:
                        // Chọn Tiếng Việt
                        setLanguage("vi");
                        break;
                    case 1:
                        // Chọn English
                        setLanguage("en");
                        break;
                }
            }
        });
        builder.show();
        mHandler = new Handler(Looper.getMainLooper());
    }
    private void setLanguage(String langCode) {
        // Lưu ngôn ngữ được chọn vào SharedPreferences hoặc các phương thức khác
        // Đồng thời cập nhật giao diện người dùng theo ngôn ngữ mới

        TextView txtLanguage = findViewById(R.id.txtLanguage);
        if (langCode.equals("en")) {
            txtLanguage.setText("English");
        } else if (langCode.equals("vi")) {
            txtLanguage.setText("Tiếng Việt");
        }
        // Cập nhật các thành phần giao diện khác tương ứng nếu cần
    }

    private void showHelpCenterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hỗ Trợ Khách Hàng");
        builder.setMessage("Bạn cần hỗ trợ gì không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hiển thị thông báo chờ trong giây lát
                AlertDialog progressDialog = new AlertDialog.Builder(SettingActivity.this)
                        .setMessage("Vui lòng đợi trong giây lát, chúng tôi sẽ liên lạc ngay!")
                        .setCancelable(false)
                        .create();
                progressDialog.show();

                // Tạo một luồng mới để giả lập thời gian chờ
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Chờ trong 3 giây (3000 milis)
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Sau khi chờ xong, hiển thị thông báo về sự cố về mạng
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                showNetworkIssueDialog();
                            }
                        });
                    }
                }).start();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showNetworkIssueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sự Cố Về Mạng");
        builder.setMessage("Hiện tại tổng đài đang gặp sự cố về mạng. Mong bạn liên lạc lại sau. Chúng tôi xin lỗi bạn vì sự cố này!.");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void showPrivacyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chính Sách Bảo Mật và Hợp Đồng");
        builder.setMessage("Chúng tôi muốn chia sẻ với bạn về hợp đồng của Công Ty SNShop, cũng như các chính sách dịch vụ và bảo vệ quyền riêng tư. Bạn có đồng ý không?");
        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hiển thị thông báo kí kết thành công
                showSuccessDialog();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hợp đồng");
        builder.setMessage("Bạn đã kí kết thành công.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void showMaintenanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Hệ thống đang được bảo trì. Vui lòng quay lại sau.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}