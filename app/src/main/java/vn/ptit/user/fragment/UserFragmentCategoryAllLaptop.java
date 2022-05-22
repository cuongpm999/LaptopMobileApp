package vn.ptit.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.ptit.R;
import vn.ptit.model.Laptop;
import vn.ptit.user.UserDetailLaptopActivity;
import vn.ptit.user.adapter.UserCategoryAdapter;

public class UserFragmentCategoryAllLaptop extends Fragment {
    private UserCategoryAdapter userCategoryAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private String tabName;
    private String manufacturerId, cpu, ram, hardDrive, vga;

    public UserFragmentCategoryAllLaptop(Context context, String tabName, String manufacturerId, String cpu, String ram, String hardDrive, String vga) {
        this.context = context;
        this.tabName = tabName;
        this.manufacturerId = manufacturerId;
        this.cpu = cpu;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.vga = vga;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_category_all_laptop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvCategoryAllLaptop);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        userCategoryAdapter = new UserCategoryAdapter(context);
        userCategoryAdapter.setLaptopClickListener(new UserCategoryAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategoryAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userCategoryAdapter);

        userCategoryAdapter.getData(tabName, manufacturerId, cpu, ram, hardDrive, vga);
    }

}
