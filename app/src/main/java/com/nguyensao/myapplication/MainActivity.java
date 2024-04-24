package com.nguyensao.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ViewPager2 viewPager2=findViewById(R.id.viewPager);
        MyViewTablayoutAdapter adapter=new MyViewTablayoutAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);

        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            if(extras.containsKey("userObject")) {
                String userObject=extras.getString("userObject");
                adapter.setMyString(userObject);
            }
        }
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.home_icon);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.order_history_icon);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.shopping_cart_24);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.account_icon2);
                        break;
                }
            }
        }).attach();
    }
}