package com.nguyensao.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nguyensao.myapplication.fagments.AccountFragment;
import com.nguyensao.myapplication.fagments.CartFragment;
import com.nguyensao.myapplication.fagments.HomeFragment;
import com.nguyensao.myapplication.fagments.SearchFragment;

public class MyViewTablayoutAdapter extends FragmentStateAdapter {
    private String myString;

    public MyViewTablayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Fragment fragment1 = new HomeFragment();
                fragment1.setArguments(creatBundle());
                return fragment1;
            case 1:
                Fragment fragment2 = new SearchFragment();
                fragment2.setArguments(creatBundle());
                return fragment2;
            case 2:
                Fragment fragment3 = new CartFragment();
                fragment3.setArguments(creatBundle());
                return fragment3;
            case 3:
                Fragment fragment4 = new AccountFragment();
                fragment4.setArguments(creatBundle());
                return fragment4;
            default:
                Fragment fragment5 = new HomeFragment();
                fragment5.setArguments(creatBundle());
                return fragment5;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private Bundle creatBundle() {
        Bundle bundle=new Bundle();
        bundle.putString("userObject",myString);
        return  bundle;
    }
    public void setMyString(String myString) {
        this.myString=myString;
    }

}