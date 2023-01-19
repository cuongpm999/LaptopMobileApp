package vn.ptit.admin.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.ptit.R;
import vn.ptit.admin.AdminAddManufacturer;
import vn.ptit.admin.AdminEditManufacturer;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminManufacturerAdapter extends RecyclerView.Adapter<AdminManufacturerAdapter.AdminManufacturerViewHolder>{
    private List<Manufacturer> mList;
    private Context context;

    public AdminManufacturerAdapter(Context context) {
        this.context = context;
        this.mList = new ArrayList<>();
        getData();
    }

    @NonNull
    @Override
    public AdminManufacturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_manufacturer,parent,false);
        return new AdminManufacturerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminManufacturerViewHolder holder, int position) {
        Manufacturer manufacturer = mList.get(position);
        holder.tv1.setText(manufacturer.getName());
        holder.tv2.setText((manufacturer.getAddress()));
        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cảnh báo xóa");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("Bạn có chắc chắn muốn xóa "+manufacturer.getName());
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = MyDomainService.name +"/api/manufacturer/delete/"+manufacturer.getId();

                        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                                getData();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "Lỗi delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue.add(stringRequest);

                    }
                });
                builder.show();

            }
        });

        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminEditManufacturer.class);
                intent.putExtra("manufacturer",manufacturer);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdminManufacturerViewHolder extends RecyclerView.ViewHolder{
        private TextView tv1,tv2;
        private ImageButton btn1, btn2;

        public AdminManufacturerViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(vn.ptit.R.id.tvNameManufacturer);
            tv2 = view.findViewById(R.id.tvAddressManufacturer);
            btn1 = view.findViewById(R.id.ibUpdateManufacturer);
            btn2 = view.findViewById(R.id.ibDelManufacturer);
        }
    }

    public void getData(){
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name +"/api/manufacturer/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.hide();

                mList.clear();
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                for (int i=0;i<response.length();i++){
                    try {
                        Manufacturer manufacturer = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Manufacturer.class);
                        mList.add(manufacturer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}
