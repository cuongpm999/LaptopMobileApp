package vn.ptit.user.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Laptop;
import vn.ptit.model.LineItem;
import vn.ptit.model.Order;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserMyOrderAdapter extends RecyclerView.Adapter<UserMyOrderAdapter.UserMyOrderViewHolder>{
    private List<Order> mList;
    private Context context;

    public UserMyOrderAdapter(Context context,String username) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData(username);
    }

    @NonNull
    @Override
    public UserMyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_myorder, parent, false);
        return new UserMyOrderAdapter.UserMyOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMyOrderViewHolder holder, int position) {
        Order order = mList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        holder.tvDateOrder.setText("Ngày mua: "+dateFormat.format(order.getCreatedAt()));
        if(order.getPayment().getType_().equalsIgnoreCase("cash"))
            holder.tvPaymentType.setText("Hình thức thanh toán: Thanh toán sau khi giao hàng");
        else if(order.getPayment().getType_().equalsIgnoreCase("digitalWallet"))
            holder.tvPaymentType.setText("Hình thức thanh toán: Thanh toán bằng PayPal");
        else if(order.getPayment().getType_().equalsIgnoreCase("credit"))
            holder.tvPaymentType.setText("Hình thức thanh toán: Thanh toán bằng thẻ tín dụng");
        holder.tvShipment.setText("Đơn vị vận chuyển: "+order.getShipment().getName()+" - "+MoneyFormat.format(order.getShipment().getPrice()) +" đ");
        holder.tvTotalMoney.setText("Tổng tiền: "+ MoneyFormat.format(order.getPayment().getTotalMoney())+" đ");
        holder.userCheckoutAdapter.setList(order.getCart().getLineItems());
        holder.tvStatusOrder.setText(order.getStatus());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserMyOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDateOrder, tvPaymentType, tvShipment, tvTotalMoney, tvStatusOrder;
        private RecyclerView recyclerView;
        private UserCheckoutAdapter userCheckoutAdapter;

        public UserMyOrderViewHolder(@NonNull View view) {
            super(view);
            tvStatusOrder = view.findViewById(R.id.tvStatusOrder);
            tvDateOrder = view.findViewById(R.id.tvDateOrder);
            tvPaymentType = view.findViewById(R.id.tvPaymentType);
            tvShipment = view.findViewById(R.id.tvShipment);
            tvTotalMoney = view.findViewById(R.id.tvTotalMoney);
            recyclerView = view.findViewById(R.id.rvMyCart);

            userCheckoutAdapter = new UserCheckoutAdapter(context);
            LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(userCheckoutAdapter);
        }
    }

    private void getData(String username){
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name +"/api/order/find-by-user/"+username;
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
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }



}
