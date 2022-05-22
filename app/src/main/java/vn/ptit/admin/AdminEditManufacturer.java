package vn.ptit.admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import vn.ptit.R;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminEditManufacturer extends AppCompatActivity implements View.OnClickListener{
    private TextView tvBack,tvId, tvDate;
    private EditText et1, et2;
    private Button btnEdit;
    private String url = MyDomainService.name +"/api/manufacturer/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_manufacturer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvBack = findViewById(R.id.tvBack);
        tvId = findViewById(R.id.tvId);
        tvDate = findViewById(R.id.tvDate);
        et1 = findViewById(R.id.etName);
        et2 = findViewById(R.id.etAddress);
        btnEdit = findViewById(R.id.btnAdd);
        btnEdit.setOnClickListener(this);
        tvBack.setOnClickListener(this);

        Intent intent = getIntent();
        Manufacturer manufacturer = (Manufacturer) intent.getSerializableExtra("manufacturer");
        tvId.setText(manufacturer.getId());
        et1.setText(manufacturer.getName());
        et2.setText(manufacturer.getAddress());
        tvDate.setText(manufacturer.getCreatedAt());
    }

    @Override
    public void onClick(View view) {
        if (view == tvBack) {
            finish();
        }

        if (view == btnEdit) {
            String name = et1.getText().toString();
            String address = et2.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập tên hãng sản xuất",Toast.LENGTH_SHORT).show();
                return;
            }
            if(address.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập địa chỉ hãng sản xuất",Toast.LENGTH_SHORT).show();
                return;
            }

            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setName(name);
            manufacturer.setAddress(address);
            manufacturer.setId(tvId.getText().toString());
            manufacturer.setStatus(true);

            manufacturer.setCreatedAt(tvDate.getText().toString());

            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            JSONObject obj = null;
            try {
                obj = new JSONObject(new Gson().toJson(manufacturer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
            System.out.println(obj);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.hide();

                    Toast.makeText(AdminEditManufacturer.this, response.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();

                    Toast.makeText(AdminEditManufacturer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonObjectRequest);
        }
    }
}