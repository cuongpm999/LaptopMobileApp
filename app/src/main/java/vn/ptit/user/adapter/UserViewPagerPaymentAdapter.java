package vn.ptit.user.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.user.fragment.UserFragmentCash;
import vn.ptit.user.fragment.UserFragmentCategory;
import vn.ptit.user.fragment.UserFragmentCredit;
import vn.ptit.user.fragment.UserFragmentDigitalWallet;
import vn.ptit.user.fragment.UserFragmentHome;
import vn.ptit.user.fragment.UserFragmentMe;

public class UserViewPagerPaymentAdapter extends FragmentStatePagerAdapter {
    private int pageNumber;
    private List<Fragment> fragmentList;
    public UserViewPagerPaymentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNumber = behavior;

        fragmentList = new ArrayList<>();
        fragmentList.add(new UserFragmentCash());
        fragmentList.add(new UserFragmentDigitalWallet());
        fragmentList.add(new UserFragmentCredit());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return fragmentList.get(0);
            case 1: return fragmentList.get(1);
            case 2: return fragmentList.get(2);
            default: return fragmentList.get(0);

        }
    }

    @Override
    public int getCount() {
        return pageNumber;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Cash";
            case 1:
                return "Digital wallet";
            case 2:
                return "Credit";
            default: return "Cash";
        }
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }
}
