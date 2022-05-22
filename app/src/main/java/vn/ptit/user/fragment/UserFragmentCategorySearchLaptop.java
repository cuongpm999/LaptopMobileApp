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
import vn.ptit.user.adapter.UserCategorySearchAdapter;

public class UserFragmentCategorySearchLaptop extends Fragment {
    private UserCategorySearchAdapter userCategorySearchAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private String tabName;
    private String name;

    public UserFragmentCategorySearchLaptop(Context context, String tabName, String name) {
        this.context = context;
        this.tabName = tabName;
        this.name = name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_category_all_laptop,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvCategoryAllLaptop);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        userCategorySearchAdapter = new UserCategorySearchAdapter(context,tabName,name);
        userCategorySearchAdapter.setLaptopClickListener(new UserCategorySearchAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategorySearchAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userCategorySearchAdapter);
    }
}
