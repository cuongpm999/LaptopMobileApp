package vn.ptit.admin.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.ptit.admin.fragment.AdminFragmentHome;
import vn.ptit.admin.fragment.AdminFragmentLaptop;
import vn.ptit.admin.fragment.AdminFragmentManufacturer;
import vn.ptit.admin.fragment.AdminFragmentOrder;
import vn.ptit.admin.fragment.AdminFragmentStat;
import vn.ptit.admin.fragment.AdminFragmentUser;

public class AdminViewPagerAdapter extends FragmentStatePagerAdapter {
    private int pageNumber;
    private Context context;

    public AdminViewPagerAdapter(@NonNull FragmentManager fm, int behavior,Context context) {
        super(fm, behavior);
        this.pageNumber = behavior;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new AdminFragmentHome(context);
            case 1: return new AdminFragmentManufacturer(context);
            case 2: return new AdminFragmentLaptop(context);
            case 3: return new AdminFragmentUser(context);
            case 4: return new AdminFragmentOrder(context);
            default: return new AdminFragmentHome(context);

        }
    }

    @Override
    public int getCount() {
        return pageNumber;
    }
}
