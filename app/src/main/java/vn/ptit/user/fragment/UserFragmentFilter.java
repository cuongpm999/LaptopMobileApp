package vn.ptit.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import vn.ptit.MainActivity;
import vn.ptit.R;
import vn.ptit.admin.adapter.AdminSpinnerManufacturerAdapter;
import vn.ptit.model.User;
import vn.ptit.user.UserEditProfileActivity;
import vn.ptit.user.UserLaptopFilterActivity;
import vn.ptit.user.UserMyOrderActivity;
import vn.ptit.user.adapter.UserSpinnerManufacturerAdapter;
import vn.ptit.util.Encoding;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserFragmentFilter extends Fragment{
    private Spinner spinner;
    private UserSpinnerManufacturerAdapter spinnerAdapter;
    private CheckBox cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7,cpu8,ram1,ram2,ram3,ram4,hd1,hd2,vga1,vga2,vga3;
    private Button btn;
    private Context context;

    public UserFragmentFilter(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.filterManufacturer);
        spinnerAdapter = new UserSpinnerManufacturerAdapter(context);
        spinner.setAdapter(spinnerAdapter);
        cpu1 = view.findViewById(R.id.cpu1);
        cpu2 = view.findViewById(R.id.cpu2);
        cpu3 = view.findViewById(R.id.cpu3);
        cpu4 = view.findViewById(R.id.cpu4);
        cpu5 = view.findViewById(R.id.cpu5);
        cpu6 = view.findViewById(R.id.cpu6);
        cpu7 = view.findViewById(R.id.cpu7);
        cpu8 = view.findViewById(R.id.cpu8);

        ram1 = view.findViewById(R.id.ram1);
        ram2 = view.findViewById(R.id.ram2);
        ram3 = view.findViewById(R.id.ram3);
        ram4 = view.findViewById(R.id.ram4);

        hd1 = view.findViewById(R.id.hd1);
        hd2 = view.findViewById(R.id.hd2);

        vga1 = view.findViewById(R.id.vga1);
        vga2 = view.findViewById(R.id.vga2);
        vga3 = view.findViewById(R.id.vga3);

        btn = view.findViewById(R.id.btnFilter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cpu = "";
                if(cpu1.isChecked()){
                    cpu+=cpu1.getText()+",";
                }
                if(cpu2.isChecked()){
                    cpu+=cpu2.getText()+",";
                }
                if(cpu3.isChecked()){
                    cpu+=cpu3.getText()+",";
                }
                if(cpu4.isChecked()){
                    cpu+=cpu4.getText()+",";
                }
                if(cpu5.isChecked()){
                    cpu+=cpu5.getText()+",";
                }
                if(cpu6.isChecked()){
                    cpu+=cpu6.getText()+",";
                }
                if(cpu7.isChecked()){
                    cpu+=cpu7.getText()+",";
                }
                if(cpu8.isChecked()){
                    cpu+=cpu8.getText()+",";
                }

                String ram ="";
                if(ram1.isChecked()){
                    ram+=ram1.getText()+",";
                }
                if(ram2.isChecked()){
                    ram+=ram2.getText()+",";
                }
                if(ram3.isChecked()){
                    ram+=ram3.getText()+",";
                }
                if(ram4.isChecked()){
                    ram+=ram4.getText()+",";
                }

                String hardDrive="";
                if(hd1.isChecked()){
                    hardDrive+=hd1.getText()+",";
                }
                if(hd2.isChecked()){
                    hardDrive+=hd2.getText()+",";
                }

                String vga="";
                if(vga1.isChecked()){
                    vga+=vga1.getText()+",";
                }
                if(vga2.isChecked()){
                    vga+=vga2.getText()+",";
                }
                if(vga3.isChecked()){
                    vga+=vga3.getText()+",";
                }


                String manufacturerId = spinnerAdapter.getManufacturer(spinner.getSelectedItemPosition()).getId();

                Intent intent = new Intent(context, UserLaptopFilterActivity.class);
                intent.putExtra("manufacturerId", manufacturerId);
                intent.putExtra("cpu",cpu);
                intent.putExtra("ram",ram);
                intent.putExtra("hardDrive",hardDrive);
                intent.putExtra("vga",vga);

                startActivity(intent);
            }
        });

    }



}
