package vn.ptit.user.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Laptop;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserSameItemAdapter extends RecyclerView.Adapter<UserSameItemAdapter.UserSameItemViewHolder> {
    private List<Laptop> mList;
    private Context context;
    private LaptopClickListener laptopClickListener;

    public UserSameItemAdapter(Context context, String idManufacturer, String idLaptop) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData(idManufacturer, idLaptop);
        System.out.println("Size laptop: " + mList.size());
    }

    @NonNull
    @Override
    public UserSameItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_in_category, parent, false);
        return new UserSameItemAdapter.UserSameItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSameItemViewHolder holder, int position) {
        Laptop laptop = mList.get(position);
        holder.tv1.setText(laptop.getName());
        if (laptop.getDiscount() > 0) {
            holder.tv2.setText(MoneyFormat.format(laptop.getPrice()) + " đ");
            holder.tv2.setPaintFlags(holder.tv2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv3.setText(laptop.getDiscount() + "%");
        } else {
            holder.tv2.setText("");
            holder.tv3.setText("");
        }
        holder.tv4.setText(MoneyFormat.format(laptop.getPrice() * (100 - laptop.getDiscount()) / 100) + " đ");

        Glide.with(context)
                .load(laptop.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserSameItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2, tv3, tv4;
        private ImageView img;

        public UserSameItemViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.nameUserItemLaptop);
            tv2 = view.findViewById(R.id.priceUserItemLaptop);
            tv3 = view.findViewById(R.id.discountUserItemLaptop);
            tv4 = view.findViewById(R.id.priceCurUserItemLaptop);
            img = view.findViewById(R.id.imgUserItemLaptop);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (laptopClickListener != null) {
                        laptopClickListener.onLaptopClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }

    public void getData(String idManufacturer, String idLaptop) {
        String url = MyDomainService.name + "/api/laptop/find-same-item?idManufacturer=" + idManufacturer + "&idLaptop=" + idLaptop;
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
                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public interface LaptopClickListener {
        public void onLaptopClick(View view, int position);
    }

    public void setLaptopClickListener(LaptopClickListener laptopClickListener) {
        this.laptopClickListener = laptopClickListener;
    }

    public Laptop getLaptop(int position) {
        return mList.get(position);
    }
}
