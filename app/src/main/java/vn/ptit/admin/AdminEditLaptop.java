package vn.ptit.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Map;

import vn.ptit.R;
import vn.ptit.admin.adapter.AdminSpinnerManufacturerAdapter;
import vn.ptit.model.Laptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.RandomString;
import vn.ptit.util.VolleySingleton;

public class AdminEditLaptop extends AppCompatActivity implements View.OnClickListener{
    private AdminSpinnerManufacturerAdapter spinnerAdapter;
    private Spinner spinner;
    private ImageButton imageButton;
    private ImageView imageView;
    private final int REQUEST_CODE_FOLDER = 696969;
    private Button button;
    private EditText etName, etCpu, etHardDrive, etRam, etVga, etPrice, etDiscount, etScreen, etVideo;
    private TextView tvBack,tvId,tvDate,tvImg;
    private String url = MyDomainService.name +"/api/laptop/update";
    private boolean isUpload = false;
    private String idManufacturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_laptop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.btnAdd);
        tvBack = findViewById(R.id.tvBack);
        tvId = findViewById(R.id.tvId);
        tvDate = findViewById(R.id.tvDate);
        tvImg = findViewById(R.id.tvImg);

        spinner = findViewById(R.id.spinnerManufacturerE);
        spinnerAdapter = new AdminSpinnerManufacturerAdapter(this,true);
        spinner.setAdapter(spinnerAdapter);
        imageButton = findViewById(R.id.ibUpload);
        imageView = findViewById(R.id.ivUpload);
        imageButton.setOnClickListener(this);
        button.setOnClickListener(this);
        tvBack.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etCpu = findViewById(R.id.etCpu);
        etHardDrive = findViewById(R.id.etHardDrive);
        etRam = findViewById(R.id.etRam);
        etVga = findViewById(R.id.etVga);
        etPrice = findViewById(R.id.etPrice);
        etDiscount = findViewById(R.id.etDiscount);
        etScreen = findViewById(R.id.etScreen);
        etVideo = findViewById(R.id.etVideo);

        Intent intent = getIntent();
        Laptop laptop = (Laptop) intent.getSerializableExtra("laptop");
        tvId.setText(laptop.getId());
        tvImg.setText(laptop.getImage());
        tvDate.setText(laptop.getCreatedAt());

        etName.setText(laptop.getName());
        etCpu.setText(laptop.getCpu());
        etRam.setText(laptop.getRam());
        etHardDrive.setText(laptop.getHardDrive());
        etVga.setText(laptop.getVga());
        etPrice.setText(laptop.getPrice()+"");
        etDiscount.setText(laptop.getDiscount()+"");
        etScreen.setText(laptop.getScreen()+"");
        etVideo.setText(laptop.getVideo());

        Glide.with(this)
                .load(laptop.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(imageView);

        idManufacturer = laptop.getManufacturer().getId();

    }

    public String getIdManufacturer() {
        return idManufacturer;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    @Override
    public void onClick(View view) {
        if (view == imageButton) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FOLDER);
        }
        if (view == button) {
            String name = etName.getText().toString();
            String cpu = etCpu.getText().toString();
            String hardDrive = etHardDrive.getText().toString();
            String ram = etRam.getText().toString();
            String vga = etVga.getText().toString();
            String video = etVideo.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cpu.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập cpu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (hardDrive.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập hard drive", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ram.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập ram", Toast.LENGTH_SHORT).show();
                return;
            }
            if (vga.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập vga", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double.parseDouble(etPrice.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Price không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double.parseDouble(etDiscount.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Discount không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double.parseDouble(etScreen.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Screen không đúng định dạng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (video.isEmpty()) {
                Toast.makeText(this, "Bạn hãy nhập video", Toast.LENGTH_SHORT).show();
                return;
            }

            Laptop laptop = new Laptop();

            if(isUpload) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                System.out.println(imageInByte.length);

                MediaManager.get().upload(imageInByte)
                        .option("resource_type", "auto")
                        .option("folder", "AndroidApps/Laptops/")
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {

                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                laptop.setImage(resultData.get("secure_url").toString());
                                laptop.setId(tvId.getText().toString());
                                laptop.setCreatedAt(tvDate.getText().toString());
                                laptop.setName(name);
                                laptop.setCpu(cpu);
                                laptop.setHardDrive(hardDrive);
                                laptop.setRam(ram);
                                laptop.setVga(vga);
                                laptop.setVideo(video);
                                laptop.setStatus(true);

                                laptop.setDiscount(Double.parseDouble(etDiscount.getText().toString()));
                                laptop.setPrice(Double.parseDouble(etPrice.getText().toString()));
                                laptop.setScreen(Double.parseDouble(etScreen.getText().toString()));

                                laptop.setManufacturer(spinnerAdapter.getManufacturer(spinner.getSelectedItemPosition()));
                                updateData(laptop);
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        })
                        .dispatch();

            }
            else{
                laptop.setImage(tvImg.getText().toString());
                laptop.setId(tvId.getText().toString());
                laptop.setCreatedAt(tvDate.getText().toString());
                laptop.setName(name);
                laptop.setCpu(cpu);
                laptop.setHardDrive(hardDrive);
                laptop.setRam(ram);
                laptop.setVga(vga);
                laptop.setVideo(video);
                laptop.setStatus(true);

                laptop.setDiscount(Double.parseDouble(etDiscount.getText().toString()));
                laptop.setPrice(Double.parseDouble(etPrice.getText().toString()));
                laptop.setScreen(Double.parseDouble(etScreen.getText().toString()));

                laptop.setManufacturer(spinnerAdapter.getManufacturer(spinner.getSelectedItemPosition()));
                updateData(laptop);
            }



        }

        if (view == tvBack) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            isUpload = true;
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void updateData(Laptop laptop){
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject obj = null;
        try {
            obj = new JSONObject(new Gson().toJson(laptop));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        System.out.println(obj);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

                Toast.makeText(AdminEditLaptop.this, response.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

                Toast.makeText(AdminEditLaptop.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}