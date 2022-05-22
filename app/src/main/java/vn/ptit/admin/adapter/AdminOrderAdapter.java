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
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditShipment;
import vn.ptit.model.Order;
import vn.ptit.model.Shipment;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder>{
    private List<Order> mList;
    private Context context;
    private OrderClickListener orderClickListener;

    public AdminOrderAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData();
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_order, parent, false);
        return new AdminOrderAdapter.AdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = mList.get(position);
        holder.tv1.setText(order.getUser().getFullName());
        holder.tv2.setText("Tổng thanh toán: "+MoneyFormat.format(order.getPayment().getTotalMoney())+" đ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        holder.tv3.setText("Ngày thanh toán: "+dateFormat.format(order.getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2,tv3 ;

        public AdminOrderViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.tvNameUserOrder);
            tv2 = view.findViewById(R.id.tvMoneyOrder);
            tv3 = view.findViewById(R.id.tvDateOrder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(orderClickListener!=null){
                        orderClickListener.onOrderClick(view,getAdapterPosition());
                    }
                }
            });
        }
    }

    public void getData() {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name +"/api/order/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.hide();

                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Order order = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Order.class);
                        mList.add(order);
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
//                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public interface OrderClickListener{
        public void onOrderClick(View view, int position);
    }

    public void setOrderClickListener(OrderClickListener orderClickListener) {
        this.orderClickListener = orderClickListener;
    }

    public Order getOrder(int position){
        return mList.get(position);
    }
}
