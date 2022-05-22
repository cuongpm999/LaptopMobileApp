package vn.ptit.admin.fragment;

import android.content.Context;
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
import vn.ptit.admin.adapter.AdminShipmentAdapter;
import vn.ptit.admin.adapter.AdminStatAdapter;
import vn.ptit.util.MyDomainService;

public class AdminFragmentStat extends Fragment {
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private Context context;
    private AdminStatAdapter adminStatAdapter1, adminStatAdapter2, adminStatAdapter3;
    public AdminFragmentStat(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_stat,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView1 = view.findViewById(R.id.statItemBestSell);
        LinearLayoutManager manager1 = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminStatAdapter1 = new AdminStatAdapter(context);
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(adminStatAdapter1);
        adminStatAdapter1.getData(MyDomainService.name + "/api/statistic/item-best-seller");


        recyclerView2 = view.findViewById(R.id.statIncomeLast5Month);
        LinearLayoutManager manager2 = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminStatAdapter2 = new AdminStatAdapter(context);
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(adminStatAdapter2);
        adminStatAdapter2.getData(MyDomainService.name + "/api/statistic/income-last-5-month");

        recyclerView3 = view.findViewById(R.id.statUserTotalMoney);
        LinearLayoutManager manager3 = new LinearLayoutManager(view.getContext(),RecyclerView.VERTICAL,false);
        adminStatAdapter3 = new AdminStatAdapter(context);
        recyclerView3.setLayoutManager(manager3);
        recyclerView3.setAdapter(adminStatAdapter3);
        adminStatAdapter3.getData(MyDomainService.name + "/api/statistic/user-total-money");
    }
}
