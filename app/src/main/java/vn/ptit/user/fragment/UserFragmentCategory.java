package vn.ptit.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import vn.ptit.R;
import vn.ptit.admin.adapter.AdminLaptopAdapter;
import vn.ptit.model.Laptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.user.UserDetailLaptopActivity;
import vn.ptit.user.UserLaptopFilterActivity;
import vn.ptit.user.adapter.UserCategoryAdapter;
import vn.ptit.user.adapter.UserCategoryInHomeAdapter;
import vn.ptit.user.adapter.UserManufacturerAdapter;
import vn.ptit.user.adapter.UserViewPagerCategoryAdapter;

public class UserFragmentCategory extends Fragment {
    private UserManufacturerAdapter userManufacturerAdapter;
    private RecyclerView recyclerView;
    private Context context;

    public UserFragmentCategory(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvUserManufacturer);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        userManufacturerAdapter = new UserManufacturerAdapter(context);
        userManufacturerAdapter.setManufacturerClickListener(new UserManufacturerAdapter.ManufacturerClickListener() {
            @Override
            public void onManufacturerClick(View view, int position) {
                Manufacturer manufacturer = userManufacturerAdapter.getManufacturer(position);
                Intent intent = new Intent(context, UserLaptopFilterActivity.class);
                intent.putExtra("manufacturerId", manufacturer.getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userManufacturerAdapter);
    }
}
