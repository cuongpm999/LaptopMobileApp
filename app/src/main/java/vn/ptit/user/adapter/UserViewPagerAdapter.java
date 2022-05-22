package vn.ptit.user.adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.ptit.admin.fragment.AdminFragmentHome;
import vn.ptit.admin.fragment.AdminFragmentLaptop;
import vn.ptit.admin.fragment.AdminFragmentManufacturer;
import vn.ptit.admin.fragment.AdminFragmentOrder;
import vn.ptit.admin.fragment.AdminFragmentUser;
import vn.ptit.user.fragment.UserFragmentCart;
import vn.ptit.user.fragment.UserFragmentCategory;
import vn.ptit.user.fragment.UserFragmentFilter;
import vn.ptit.user.fragment.UserFragmentHome;
import vn.ptit.user.fragment.UserFragmentMe;

public class UserViewPagerAdapter extends FragmentStatePagerAdapter {
    private int pageNumber;
    private Context context;
    private SharedPreferences sharedPreferences;

    public UserViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context, SharedPreferences sharedPreferences) {
        super(fm, behavior);
        this.pageNumber = behavior;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserFragmentHome(context);
            case 1: return new UserFragmentCategory(context);
            case 2: return new UserFragmentFilter(context);
            case 3: return new UserFragmentMe(sharedPreferences,context);
            default: return new UserFragmentHome(context);

        }
    }

    @Override
    public int getCount() {
        return pageNumber;
    }
}
