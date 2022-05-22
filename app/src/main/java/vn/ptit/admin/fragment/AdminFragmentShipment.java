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
import vn.ptit.admin.AdminAddShipment;
import vn.ptit.admin.AdminAddUser;
import vn.ptit.admin.adapter.AdminShipmentAdapter;
import vn.ptit.admin.adapter.AdminUserAdapter;

public class AdminFragmentShipment extends Fragment {
    private RecyclerView recyclerView;
    private Context context;
    private AdminShipmentAdapter adminShipmentAdapter;
    private Button btnAdd;

    public AdminFragmentShipment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_shipment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdd = view.findViewById(R.id.btnAddShipment);
        recyclerView = view.findViewById(R.id.rvAdminShipment);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminShipmentAdapter = new AdminShipmentAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adminShipmentAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminAddShipment.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        adminShipmentAdapter.getData();
    }
}
