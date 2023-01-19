package vn.ptit.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import vn.ptit.R;
import vn.ptit.model.User;
import vn.ptit.util.Encoding;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText etOldPassword, etNewPassword;
    private Button button;
    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNewPassword = findViewById(R.id.etNewPassword);
        etOldPassword = findViewById(R.id.etOldPassword);
        button = findViewById(R.id.btnAdd);
        tvBack = findViewById(R.id.tvBack);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        if(user.getPassword().equalsIgnoreCase("N/A")){
            etOldPassword.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etOldPassword.getText().toString().isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this,"Bạn hãy nhập old password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etNewPassword.getText().toString().isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this,"Bạn hãy nhập new password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!user.getPassword().equalsIgnoreCase("N/A")) {
                    if (!etOldPassword.getText().toString().equalsIgnoreCase(user.getPassword())) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                putPassword(user);
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void putPassword(User user) {
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = MyDomainService.name + "/api/user/change-password?password=" + etNewPassword.getText().toString() + "&userId=" + user.getId();

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

}