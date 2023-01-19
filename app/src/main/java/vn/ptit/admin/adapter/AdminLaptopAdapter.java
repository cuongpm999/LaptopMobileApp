package vn.ptit.admin.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.admin.AdminEditManufacturer;
import vn.ptit.model.Laptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminLaptopAdapter extends RecyclerView.Adapter<AdminLaptopAdapter.AdminLaptopViewHolder>{
    private List<Laptop> mList;
    private Context context;
    private List<Laptop> mListBackup;

    public AdminLaptopAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData();
        System.out.println("Size laptop: " + mList.size());
        mListBackup = mList;
    }

    @NonNull
    @Override
    public AdminLaptopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_laptop, parent, false);
        return new AdminLaptopAdapter.AdminLaptopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminLaptopViewHolder holder, int position) {
        Laptop laptop = mList.get(position);
        holder.tv1.setText(laptop.getName());
        holder.tv2.setText(MoneyFormat.format(laptop.getPrice()) + " đ");

        Glide.with(context)
                .load(laptop.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cảnh báo xóa");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("Bạn có chắc chắn muốn xóa " + laptop.getName());
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = MyDomainService.name +"/api/laptop/delete/" + laptop.getId();

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
                                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(context, AdminEditLaptop.class);
                intent.putExtra("laptop", laptop);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdminLaptopViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;
        private ImageView img;
        private ImageButton btn1, btn2;

        public AdminLaptopViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.nameAdminItemLaptop);
            tv2 = view.findViewById(R.id.priceAdminItemLaptop);
            img = view.findViewById(R.id.imgAdminItemLaptop);
            btn1 = view.findViewById(R.id.ibUpdateLaptop);
            btn2 = view.findViewById(R.id.ibDelLaptop);
        }
    }

    public void getData() {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name +"/api/laptop/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.hide();

                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Laptop laptop = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Laptop.class);
                        mList.add(laptop);
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

    public void findByName(String key) {
        String url = MyDomainService.name +"/api/laptop/find-by-name/" + key;
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Laptop laptop = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Laptop.class);
                        mList.add(laptop);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


}
