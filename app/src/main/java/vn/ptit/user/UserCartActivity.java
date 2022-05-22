package vn.ptit.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Cart;
import vn.ptit.model.LineItem;
import vn.ptit.model.User;
import vn.ptit.user.adapter.UserCartAdapter;
import vn.ptit.user.adapter.UserCategoryAdapter;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private UserCartAdapter userCartAdapter;
    private TextView tvTotalAmount;
    private TextView textCartItemCount;
    private TextView tvCheckout;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);
        recyclerView = findViewById(R.id.rvUserCart);
        userCartAdapter = new UserCartAdapter(this,sharedPreferences);
        LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userCartAdapter);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvCheckout = findViewById(R.id.tvCheckout);
        img = findViewById(R.id.ivNoCart);
        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userCartAdapter.getItemCount()==0){
                    Toast.makeText(UserCartActivity.this,"Bạn hãy chọn hàng cần mua",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(UserCartActivity.this,UserCheckoutActivity.class);
                startActivity(intent);
            }
        });
        getData();
    }

    public void getData(){
        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/cart/find-current?username="+username;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Cart cart = new Gson().fromJson(response, Cart.class);
                if(cart==null) {
                    userCartAdapter.setList(new ArrayList<>());
                    tvTotalAmount.setText("0 đ");
//                    setupBadge(0);
                    recyclerView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    return;
                }
                List<LineItem> list = cart.getLineItems();
                int totalQuantity = 0;
                for (LineItem l : list) {
                    totalQuantity += l.getQuantity();
                }
//                setupBadge(totalQuantity);
                userCartAdapter.setList(cart.getLineItems());
                tvTotalAmount.setText(MoneyFormat.format(cart.getTotalAmount())+" đ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserCartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.user_menu, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.mUserCart);
//        View actionView = menuItem.getActionView();
//        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
//
//        getData();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.mUserCart:
//                Intent intent = new Intent(this, UserCartActivity.class);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void setupBadge(int mCartItemCount) {
//
//        if (textCartItemCount != null) {
//            if (mCartItemCount == 0) {
//                if (textCartItemCount.getVisibility() != View.GONE) {
//                    textCartItemCount.setVisibility(View.GONE);
//                }
//            } else {
//                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
//                if (textCartItemCount.getVisibility() != View.VISIBLE) {
//                    textCartItemCount.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}