package vn.ptit.user.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.model.Cart;
import vn.ptit.model.Laptop;
import vn.ptit.model.LineItem;
import vn.ptit.user.UserCartActivity;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.UserCartViewHolder>{
    private List<LineItem> mList;
    private Context context;
    private SharedPreferences sharedPreferences;

    public UserCartAdapter(Context context,SharedPreferences sharedPreferences) {
        this.mList = new ArrayList<>();
        this.context = context;
        this.sharedPreferences= sharedPreferences;
    }

    public void setList(List<LineItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_cart, parent, false);
        return new UserCartAdapter.UserCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCartViewHolder holder, int position) {
        LineItem lineItem = mList.get(position);
        holder.tv1.setText(lineItem.getLaptop().getName());
        holder.tv2.setText(MoneyFormat.format(lineItem.getLaptop().getPrice()*lineItem.getQuantity()*(100-lineItem.getLaptop().getDiscount())/100) + " đ");
        holder.tv3.setText(lineItem.getQuantity()+"");

        Glide.with(context)
                .load(lineItem.getLaptop().getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.img);

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cảnh báo xóa");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("Bạn có chắc chắn muốn xóa " + lineItem.getLaptop().getName());
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressDialog pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        String username = sharedPreferences.getString("usernameLogin", "");
                        String url = MyDomainService.name + "/api/cart/delete?username="+username+"&laptopId="+lineItem.getLaptop().getId();
                        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.hide();

                                UserCartActivity userCartActivity = (UserCartActivity) context;
                                userCartActivity.getData();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.hide();

                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue.add(stringRequest);

                    }
                });
                builder.show();

            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.show();

                int quantityEdit = Integer.parseInt(holder.tv3.getText().toString())+1;
                holder.tv3.setText(quantityEdit+"");

                String username = sharedPreferences.getString("usernameLogin", "");
                String url = MyDomainService.name + "/api/cart/edit?username="+username+"&laptopId="+lineItem.getLaptop().getId()+"&quantity="+quantityEdit;
                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();

                        UserCartActivity userCartActivity = (UserCartActivity) context;
                        userCartActivity.getData();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);

            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.tv3.getText().toString()) == 1) {
                    return;
                }

                ProgressDialog pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.show();

                int quantityEdit = Integer.parseInt(holder.tv3.getText().toString())-1;
                holder.tv3.setText(quantityEdit+"");
                String username = sharedPreferences.getString("usernameLogin", "");
                String url = MyDomainService.name + "/api/cart/edit?username="+username+"&laptopId="+lineItem.getLaptop().getId()+"&quantity="+quantityEdit;
                RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();

                        UserCartActivity userCartActivity = (UserCartActivity) context;
                        userCartActivity.getData();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(stringRequest);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class UserCartViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2, tv3;
        private ImageView img;
        private ImageButton btnDel, btnPlus, btnMinus;

        public UserCartViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.nameCartItemLaptop);
            tv2 = view.findViewById(R.id.priceCartItemLaptop);
            img = view.findViewById(R.id.imgCartItemLaptop);
            tv3 = view.findViewById(R.id.tvQuantity);
            btnDel = view.findViewById(R.id.ibCartDelLaptop);
            btnPlus = view.findViewById(R.id.ibPlus);
            btnMinus = view.findViewById(R.id.ibMinus);
        }
    }

}
