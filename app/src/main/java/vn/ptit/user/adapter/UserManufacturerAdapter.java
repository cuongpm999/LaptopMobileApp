package vn.ptit.user.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Laptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserManufacturerAdapter extends RecyclerView.Adapter<UserManufacturerAdapter.UserManufacturerViewHolder>{
    private List<Manufacturer> mList;
    private Context context;
    private ManufacturerClickListener manufacturerClickListener;

    public UserManufacturerAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData();
    }

    @NonNull
    @Override
    public UserManufacturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_manufacturer, parent, false);
        return new UserManufacturerAdapter.UserManufacturerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserManufacturerViewHolder holder, int position) {
        Manufacturer manufacturer = mList.get(position);
        holder.tv1.setText(manufacturer.getName().toUpperCase());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserManufacturerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1;

        public UserManufacturerViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.tvManufacturerItemName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(manufacturerClickListener!=null){
                        manufacturerClickListener.onManufacturerClick(view,getAdapterPosition());
                    }
                }
            });
        }
    }

    public void getData() {
        String url = MyDomainService.name +"/api/manufacturer/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mList.clear();
                for (int i = 0; i < response.length(); i++) {
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
//                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public interface ManufacturerClickListener{
        public void onManufacturerClick(View view, int position);
    }

    public void setManufacturerClickListener(ManufacturerClickListener manufacturerClickListener) {
        this.manufacturerClickListener = manufacturerClickListener;
    }

    public Manufacturer getManufacturer(int position){
        return mList.get(position);
    }
}
