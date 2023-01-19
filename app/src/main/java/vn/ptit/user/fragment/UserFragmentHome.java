package vn.ptit.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import vn.ptit.R;
import vn.ptit.admin.AdminAddLaptop;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.admin.adapter.AdminLaptopAdapter;
import vn.ptit.model.Laptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.user.UserDetailLaptopActivity;
import vn.ptit.user.UserLaptopFilterActivity;
import vn.ptit.user.adapter.SliderAdapter;
import vn.ptit.user.adapter.UserCategoryInHomeAdapter;

public class UserFragmentHome extends Fragment implements View.OnClickListener{
    private SliderView sliderView;
    private Context context;
    private RecyclerView recyclerViewAsus;
    private RecyclerView recyclerViewDell;
    private RecyclerView recyclerViewHP;
    private RecyclerView recyclerViewMSI;

    private TextView tv1,tv2,tv3,tv4;

    private UserCategoryInHomeAdapter userCategoryAsusInHomeAdapter;
    private UserCategoryInHomeAdapter userCategoryDellInHomeAdapter;
    private UserCategoryInHomeAdapter userCategoryHPInHomeAdapter;
    private UserCategoryInHomeAdapter userCategoryMSIInHomeAdapter;

    public UserFragmentHome(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.user_fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sliderView = view.findViewById(R.id.imageSlider);

        SliderAdapter sliderAdapter = new SliderAdapter();
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        LinearLayoutManager managerAsus = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager managerDell = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager managerHP = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL,false);
        LinearLayoutManager managerMSI = new LinearLayoutManager(view.getContext(),RecyclerView.HORIZONTAL,false);

        recyclerViewAsus = view.findViewById(R.id.rvUserCategoryAsus);
        userCategoryAsusInHomeAdapter = new UserCategoryInHomeAdapter(context,"1");
        userCategoryAsusInHomeAdapter.setLaptopClickListener(new UserCategoryInHomeAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategoryAsusInHomeAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerViewAsus.setLayoutManager(managerAsus);
        recyclerViewAsus.setAdapter(userCategoryAsusInHomeAdapter);

        recyclerViewDell = view.findViewById(R.id.rvUserCategoryDell);
        userCategoryDellInHomeAdapter = new UserCategoryInHomeAdapter(context,"2");
        userCategoryDellInHomeAdapter.setLaptopClickListener(new UserCategoryInHomeAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategoryDellInHomeAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerViewDell.setLayoutManager(managerDell);
        recyclerViewDell.setAdapter(userCategoryDellInHomeAdapter);

        recyclerViewHP = view.findViewById(R.id.rvUserCategoryHP);
        userCategoryHPInHomeAdapter = new UserCategoryInHomeAdapter(context,"3");
        userCategoryHPInHomeAdapter.setLaptopClickListener(new UserCategoryInHomeAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategoryHPInHomeAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerViewHP.setLayoutManager(managerHP);
        recyclerViewHP.setAdapter(userCategoryHPInHomeAdapter);

        recyclerViewMSI = view.findViewById(R.id.rvUserCategoryMSI);
        userCategoryMSIInHomeAdapter = new UserCategoryInHomeAdapter(context,"4");
        userCategoryMSIInHomeAdapter.setLaptopClickListener(new UserCategoryInHomeAdapter.LaptopClickListener() {
            @Override
            public void onLaptopClick(View view, int position) {
                Laptop laptop = userCategoryMSIInHomeAdapter.getLaptop(position);
                Intent intent = new Intent(context, UserDetailLaptopActivity.class);
                intent.putExtra("laptop", laptop);
                startActivity(intent);
            }
        });
        recyclerViewMSI.setLayoutManager(managerMSI);
        recyclerViewMSI.setAdapter(userCategoryMSIInHomeAdapter);

        tv1 = view.findViewById(R.id.tvManuAsus);
        tv2 = view.findViewById(R.id.tvManuDell);
        tv3 = view.findViewById(R.id.tvManuHP);
        tv4 = view.findViewById(R.id.tvManuMSI);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == tv1){
            Intent intent = new Intent(context, UserLaptopFilterActivity.class);
            intent.putExtra("manufacturerId", "1");
            startActivity(intent);
        }
        if(view == tv2){
            Intent intent = new Intent(context, UserLaptopFilterActivity.class);
            intent.putExtra("manufacturerId", "2");
            startActivity(intent);
        }
        if(view == tv3){
            Intent intent = new Intent(context, UserLaptopFilterActivity.class);
            intent.putExtra("manufacturerId", "3");
            startActivity(intent);
        }
        if(view == tv4){
            Intent intent = new Intent(context, UserLaptopFilterActivity.class);
            intent.putExtra("manufacturerId", "4");
            startActivity(intent);
        }
    }
}
