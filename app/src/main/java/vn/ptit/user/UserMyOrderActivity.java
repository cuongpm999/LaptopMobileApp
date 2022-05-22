package vn.ptit.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import vn.ptit.R;
import vn.ptit.user.adapter.UserCheckoutAdapter;
import vn.ptit.user.adapter.UserMyOrderAdapter;

public class UserMyOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserMyOrderAdapter userMyOrderAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rvMyOrder);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);
        String username = sharedPreferences.getString("usernameLogin", "");

        userMyOrderAdapter = new UserMyOrderAdapter(this,username);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userMyOrderAdapter);
    }
}