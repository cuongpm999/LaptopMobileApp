package vn.ptit.admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.ptit.R;
import vn.ptit.admin.AdminAddLaptop;
import vn.ptit.admin.AdminAddUser;
import vn.ptit.admin.adapter.AdminLaptopAdapter;
import vn.ptit.admin.adapter.AdminUserAdapter;

public class AdminFragmentUser extends Fragment {
    private RecyclerView recyclerView;
    private Context context;
    private AdminUserAdapter adminUserAdapter;
    private Button btnAdd;

    public AdminFragmentUser(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_user,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAddUser);
        recyclerView = view.findViewById(R.id.rvAdminUser);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminUserAdapter = new AdminUserAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adminUserAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminAddUser.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        adminUserAdapter.getData();
    }
}
