package vn.ptit.admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.ptit.R;
import vn.ptit.admin.AdminOrderItem;
import vn.ptit.admin.adapter.AdminOrderAdapter;
import vn.ptit.admin.adapter.AdminShipmentAdapter;
import vn.ptit.model.Order;
import vn.ptit.user.UserDetailLaptopActivity;

public class AdminFragmentOrder extends Fragment {
    private RecyclerView recyclerView;
    private Context context;
    private AdminOrderAdapter adminOrderAdapter;
    public AdminFragmentOrder(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_order,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvAdminOrder);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminOrderAdapter = new AdminOrderAdapter(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adminOrderAdapter);

        adminOrderAdapter.setOrderClickListener(new AdminOrderAdapter.OrderClickListener() {
            @Override
            public void onOrderClick(View view, int position) {
                Order order = adminOrderAdapter.getOrder(position);
                Intent intent = new Intent(context, AdminOrderItem.class);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });
    }
}
