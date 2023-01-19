package vn.ptit.user.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vn.ptit.user.fragment.UserFragmentCart;
import vn.ptit.user.fragment.UserFragmentCategory;
import vn.ptit.user.fragment.UserFragmentCategoryAllLaptop;
import vn.ptit.user.fragment.UserFragmentHome;
import vn.ptit.user.fragment.UserFragmentMe;

public class UserViewPagerCategoryAdapter extends FragmentStatePagerAdapter {
    private int pageNumber;
    private Context context;
    private String manufacturerId,cpu,ram,hardDrive,vga;

    public UserViewPagerCategoryAdapter(@NonNull FragmentManager fm, int behavior, Context context,String manufacturerId,String cpu,String ram, String hardDrive,String vga) {
        super(fm, behavior);
        this.pageNumber = behavior;
        this.context = context;
        this.manufacturerId = manufacturerId;
        this.cpu = cpu;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.vga = vga;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new UserFragmentCategoryAllLaptop(context,"new",manufacturerId,cpu,ram,hardDrive,vga);
            case 1: return new UserFragmentCategoryAllLaptop(context,"discount",manufacturerId,cpu,ram,hardDrive,vga);
            case 2: return new UserFragmentCategoryAllLaptop(context,"low-to-high",manufacturerId,cpu,ram,hardDrive,vga);
            case 3: return new UserFragmentCategoryAllLaptop(context,"high-to-low",manufacturerId,cpu,ram,hardDrive,vga);
            default: return new UserFragmentCategoryAllLaptop(context,"new",manufacturerId,cpu,ram,hardDrive,vga);

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
