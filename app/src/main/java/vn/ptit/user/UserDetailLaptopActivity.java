package vn.ptit.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import vn.ptit.R;
import vn.ptit.model.Cart;
import vn.ptit.model.Laptop;
import vn.ptit.model.LineItem;
import vn.ptit.model.User;
import vn.ptit.user.adapter.UserCategoryInHomeAdapter;
import vn.ptit.user.adapter.UserSameItemAdapter;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserDetailLaptopActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton imageButton;
    private TextView textView;
    private SharedPreferences sharedPreferences;
    private String laptopId;
    private TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_laptop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageButton = findViewById(R.id.ibCart);
        textView = findViewById(R.id.tvCartNow);

        imageButton.setOnClickListener(this);
        textView.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);

        Intent intent = getIntent();
        Laptop laptop = (Laptop) intent.getSerializableExtra("laptop");
        laptopId = laptop.getId();

    }

    @Override
    public void onClick(View view) {
        if(view==imageButton){
            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            String username = sharedPreferences.getString("usernameLogin", "");
            String url = MyDomainService.name + "/api/cart/add?username="+username+"&laptopId="+laptopId;
            RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();

                    Toast.makeText(UserDetailLaptopActivity.this, "Bạn đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    getTotalQuantity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();

                    Toast.makeText(UserDetailLaptopActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
        }
        if(view == textView){
            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            String username = sharedPreferences.getString("usernameLogin", "");
            String url = MyDomainService.name + "/api/cart/add?username="+username+"&laptopId="+laptopId;
            RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();

                    Toast.makeText(UserDetailLaptopActivity.this, "Bạn đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    getTotalQuantity();
                    Intent intent = new Intent(UserDetailLaptopActivity.this, UserCartActivity.class);
                    startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();

                    Toast.makeText(UserDetailLaptopActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
        }
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
                Toast.makeText(UserDetailLaptopActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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