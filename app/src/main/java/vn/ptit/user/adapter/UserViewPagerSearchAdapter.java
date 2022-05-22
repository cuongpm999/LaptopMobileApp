package vn.ptit.user.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.ptit.user.fragment.UserFragmentCategoryAllLaptop;
import vn.ptit.user.fragment.UserFragmentCategorySearchLaptop;

public class UserViewPagerSearchAdapter extends FragmentStatePagerAdapter {
    private int pageNumber;
    private Context context;
    private String name;

    public UserViewPagerSearchAdapter(@NonNull FragmentManager fm, int behavior, Context context, String name) {
        super(fm, behavior);
        this.pageNumber = behavior;
        this.context = context;
        this.name = name;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserFragmentCategorySearchLaptop(context,"new",name);
            case 1: return new UserFragmentCategorySearchLaptop(context,"discount",name);
            case 2: return new UserFragmentCategorySearchLaptop(context,"low-to-high",name);
            case 3: return new UserFragmentCategorySearchLaptop(context,"high-to-low",name);
            default: return new UserFragmentCategorySearchLaptop(context,"new",name);

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
                return "Mới nhất";
            case 1:
                return "Khuyến mại";
            case 2:
                return "Giá tăng";
            case 3:
                return "Giá giảm";
            default: return "Mới nhất";
        }
    }

}
