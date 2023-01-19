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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.admin.AdminEditUser;
import vn.ptit.model.Laptop;
import vn.ptit.model.User;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.AdminUserViewHolder>{
    private List<User> mList;
    private Context context;

    public AdminUserAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData();
    }

    @NonNull
    @Override
    public AdminUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_user, parent, false);
        return new AdminUserAdapter.AdminUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserViewHolder holder, int position) {
        User user = mList.get(position);
        holder.tv1.setText(user.getFullName());
        holder.tv2.setText(user.getUsername());
        if(user.isSex())
            holder.sexImg.setImageResource(R.drawable.male);
        else
            holder.sexImg.setImageResource(R.drawable.female);

        Glide.with(context)
                .load(user.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

        holder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cảnh báo xóa");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("Bạn có chắc chắn muốn xóa " + user.getFullName());
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = MyDomainService.name +"/api/user/delete/" + user.getId();

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
                Intent intent = new Intent(context, AdminEditUser.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdminUserViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2 ;
        private ImageView img,sexImg;
        private ImageButton btn1, btn2;

        public AdminUserViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.nameAdminItemUser);
            tv2 = view.findViewById(R.id.usernameAdminItemUser);
            img = view.findViewById(R.id.imgAdminItemUser);
            sexImg = view.findViewById(R.id.sexAdminItemUser);
            btn1 = view.findViewById(R.id.ibUpdateUser);
            btn2 = view.findViewById(R.id.ibDelUser);
        }
    }

    public void getData() {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name +"/api/user/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.hide();

                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        User user = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), User.class);
                        mList.add(user);
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
