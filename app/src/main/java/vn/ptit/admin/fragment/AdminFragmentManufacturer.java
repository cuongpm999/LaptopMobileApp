package vn.ptit.admin.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminAddManufacturer;
import vn.ptit.admin.adapter.AdminManufacturerAdapter;
import vn.ptit.model.Manufacturer;

public class AdminFragmentManufacturer extends Fragment {
    private AdminManufacturerAdapter manufacturerAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private Button btnAdd;

    public AdminFragmentManufacturer(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.admin_fragment_manufacturer,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAddManufacturer);

        recyclerView = view.findViewById(R.id.rvAdminManufacturer);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        manufacturerAdapter = new AdminManufacturerAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(manufacturerAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminAddManufacturer.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        manufacturerAdapter.getData();
    }
}
