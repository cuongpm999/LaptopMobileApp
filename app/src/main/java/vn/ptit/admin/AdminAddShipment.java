package vn.ptit.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.ptit.R;
import vn.ptit.model.Shipment;
import vn.ptit.model.User;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.RandomString;
import vn.ptit.util.VolleySingleton;

public class AdminAddShipment extends AppCompatActivity implements View.OnClickListener{
    private EditText etName,etAddress,etPrice;
    private String url = MyDomainService.name + "/api/shipment/insert";
    private TextView tvBack;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_shipment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPrice = findViewById(R.id.etPrice);

        tvBack = findViewById(R.id.tvBack);
        button = findViewById(R.id.btnAdd);
        tvBack.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == button) {
            String name = etName.getText().toString();
            String address = etAddress.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập name",Toast.LENGTH_SHORT).show();
                return;
            }
            if(address.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập address",Toast.LENGTH_SHORT).show();
                return;
            }
            double price = 0;
            try{
                price = Double.parseDouble(etPrice.getText().toString());
            }
            catch (Exception e){
                Toast.makeText(this,"Price không đúng định dạng",Toast.LENGTH_SHORT).show();
                return;
            }

            Shipment shipment = new Shipment();

            shipment.setAddress(address);
            shipment.setName(name);
            shipment.setPrice(price);

            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            JSONObject obj = null;
            try {
                obj = new JSONObject(new Gson().toJson(shipment));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
            System.out.println(obj);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.hide();

                    Toast.makeText(AdminAddShipment.this, response.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();

                    Toast.makeText(AdminAddShipment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(jsonObjectRequest);
        }

        if (view == tvBack) {
            finish();
        }
    }
}