package vn.ptit.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Cart;
import vn.ptit.model.LineItem;
import vn.ptit.user.adapter.UserViewPagerCategoryAdapter;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserLaptopFilterActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private UserViewPagerCategoryAdapter adapter;
    private TextView textCartItemCount;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_laptop_filter);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayoutCategory);
        viewPager = findViewById(R.id.viewPagerCategory);
        Intent intent = getIntent();
        String manufacturerId = intent.getStringExtra("manufacturerId");
        String cpu = intent.getStringExtra("cpu");
        String ram = intent.getStringExtra("ram");
        String hardDrive = intent.getStringExtra("hardDrive");
        String vga = intent.getStringExtra("vga");
        adapter = new UserViewPagerCategoryAdapter(getSupportFragmentManager(), 4, this, manufacturerId,cpu,ram,hardDrive,vga);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.mUserCart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        getTotalQuantity();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mUserCart:
                Intent intent = new Intent(this, UserCartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge(int mCartItemCount) {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void getTotalQuantity() {
        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/cart/find-current?username=" + username;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Cart cart = new Gson().fromJson(response, Cart.class);
                if (cart == null) {
                    setupBadge(0);
                    return;
                }
                List<LineItem> list = cart.getLineItems();
                int totalQuantity = 0;
                for (LineItem l : list) {
                    totalQuantity += l.getQuantity();
                }
                setupBadge(totalQuantity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserLaptopFilterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTotalQuantity();
    }

}