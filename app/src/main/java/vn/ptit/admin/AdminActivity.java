package vn.ptit.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import vn.ptit.MainActivity;
import vn.ptit.R;
import vn.ptit.admin.adapter.AdminViewPagerAdapter;
import vn.ptit.admin.fragment.AdminFragmentHome;
import vn.ptit.admin.fragment.AdminFragmentLaptop;
import vn.ptit.admin.fragment.AdminFragmentManufacturer;
import vn.ptit.admin.fragment.AdminFragmentOrder;
import vn.ptit.admin.fragment.AdminFragmentShipment;
import vn.ptit.admin.fragment.AdminFragmentStat;
import vn.ptit.admin.fragment.AdminFragmentUser;
import vn.ptit.config.CloudinaryConfig;
import vn.ptit.model.User;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MANUFACTURER = 1;
    private static final int FRAGMENT_LAPTOP = 2;
    private static final int FRAGMENT_USER = 3;
    private static final int FRAGMENT_ORDER = 4;
    private static final int FRAGMENT_SHIPMENT = 5;
    private static final int FRAGMENT_STAT = 6;

    private int mCurrentFragment = FRAGMENT_HOME;
    private ImageView adminAvatar;
    private TextView adminUsername;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new AdminFragmentHome(this.getApplicationContext()));
        navigationView.getMenu().findItem(R.id.mAdminHome).setChecked(true);
        View header = navigationView.getHeaderView(0);
        adminAvatar = header.findViewById(R.id.adminAvatar);
        adminUsername = header.findViewById(R.id.adminUsername);

        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/user/find-by-username/" + username;

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AdminActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                User user = new Gson().fromJson(response, User.class);

                Glide.with(AdminActivity.this)
                        .load(user.getImage())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(adminAvatar);

                adminUsername.setText(username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mAdminHome:
                if(mCurrentFragment !=FRAGMENT_HOME){
                    replaceFragment(new AdminFragmentHome(this));
                    mCurrentFragment = FRAGMENT_HOME;
                }
                break;
            case R.id.mAdminManufacturer:
                if(mCurrentFragment !=FRAGMENT_MANUFACTURER){
                    replaceFragment(new AdminFragmentManufacturer(this));
                    mCurrentFragment = FRAGMENT_MANUFACTURER;
                }
                break;
            case R.id.mAdminlaptop:
                if(mCurrentFragment !=FRAGMENT_LAPTOP){
                    replaceFragment(new AdminFragmentLaptop(this));
                    mCurrentFragment = FRAGMENT_LAPTOP;
                }
                break;
            case R.id.mAdminUser:
                if(mCurrentFragment !=FRAGMENT_USER){
                    replaceFragment(new AdminFragmentUser(this));
                    mCurrentFragment = FRAGMENT_USER;
                }
                break;
            case R.id.mAdminShipment:
                if(mCurrentFragment !=FRAGMENT_SHIPMENT){
                    replaceFragment(new AdminFragmentShipment(this));
                    mCurrentFragment = FRAGMENT_SHIPMENT;
                }
                break;
            case R.id.mAdminOrder:
                if(mCurrentFragment !=FRAGMENT_ORDER){
                    replaceFragment(new AdminFragmentOrder(this));
                    mCurrentFragment = FRAGMENT_ORDER;
                }
                break;
            case R.id.mAdminStat:
                if(mCurrentFragment !=FRAGMENT_STAT){
                    replaceFragment(new AdminFragmentStat(this));
                    mCurrentFragment = FRAGMENT_STAT;
                }
                break;
            case R.id.mAdminLogout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("usernameLogin");
                editor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
}