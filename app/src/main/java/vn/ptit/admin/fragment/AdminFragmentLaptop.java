package vn.ptit.admin.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminAddLaptop;
import vn.ptit.admin.AdminAddManufacturer;
import vn.ptit.admin.adapter.AdminLaptopAdapter;
import vn.ptit.admin.adapter.AdminManufacturerAdapter;
import vn.ptit.model.Laptop;

public class AdminFragmentLaptop extends Fragment {
    private AdminLaptopAdapter adminLaptopAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private Button btnAdd;
    private SearchView searchView;

    public AdminFragmentLaptop(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.admin_fragment_laptop,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAdd = view.findViewById(R.id.btnAddLaptop);

        recyclerView = view.findViewById(R.id.rvAdminLaptop);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminLaptopAdapter = new AdminLaptopAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adminLaptopAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminAddLaptop.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.admin_menu_laptop,menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.mAdminLaptopSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()) adminLaptopAdapter.getData();
                else adminLaptopAdapter.findByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) adminLaptopAdapter.getData();
                else adminLaptopAdapter.findByName(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        adminLaptopAdapter.getData();
    }

}
