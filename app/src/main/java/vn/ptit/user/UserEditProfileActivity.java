package vn.ptit.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import vn.ptit.R;
import vn.ptit.RegisterActivity;
import vn.ptit.model.User;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.RandomString;
import vn.ptit.util.VolleySingleton;

public class UserEditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName, etAddress, etEmail, etMobile, etDateOfBirth, etUsername;
    private Button button;
    private RadioButton rb1, rb2;
    private TextView tvBack, tvImg, tvId, tvPassword;
    private String url = MyDomainService.name + "/api/user/update";
    private final int REQUEST_CODE_FOLDER = 3993;
    private final int REQUEST_CODE_CAMERA = 699996;
    private ImageButton ib1, ib2;
    private ImageView imageView;
    private boolean isUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        etUsername = findViewById(R.id.etUsername);
        tvPassword = findViewById(R.id.tvPassword);

        button = findViewById(R.id.btnAdd);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);

        tvBack = findViewById(R.id.tvBack);
        tvId = findViewById(R.id.tvId);
        tvImg = findViewById(R.id.tvImg);

        etDateOfBirth.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        button.setOnClickListener(this);

        ib1 = findViewById(R.id.ibUpload);
        ib1.setOnClickListener(this);
        ib2 = findViewById(R.id.ibCamera);
        ib2.setOnClickListener(this);
        imageView = findViewById(R.id.ivUpload);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        tvId.setText(user.getId());
        tvImg.setText(user.getImage());
        etName.setText(user.getFullName());
        etAddress.setText(user.getAddress());
        etEmail.setText(user.getEmail());
        etMobile.setText(user.getMobile());
        etUsername.setText(user.getUsername());
        if (user.isSex()) rb1.setChecked(true);
        else rb2.setChecked(true);
        tvPassword.setText(user.getPassword());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf1.parse(user.getDateOfBirth());
            etDateOfBirth.setText(sdf.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load(user.getImage())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(imageView);

    }

    @Override
    public void onClick(View view) {
        if (view == ib1) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FOLDER);
        }
        if (view == ib2) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
        if (view == etDateOfBirth) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(i, i1, i2);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    etDateOfBirth.setText(sdf.format(calendar.getTime()));
                }
            }, year, month, day);
            dialog.show();
        }

        if (view == button) {
            User user = new User();

            String address = etAddress.getText().toString();
            String dateOfBirth = etDateOfBirth.getText().toString();
            String email = etEmail.getText().toString();
            String name = etName.getText().toString();
            String mobile = etMobile.getText().toString();
            String username = etUsername.getText().toString();

            if(name.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập full name",Toast.LENGTH_SHORT).show();
                return;
            }
            if(address.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập address",Toast.LENGTH_SHORT).show();
                return;
            }
            if(email.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập email",Toast.LENGTH_SHORT).show();
                return;
            }
            if(mobile.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập mobile",Toast.LENGTH_SHORT).show();
                return;
            }
            if(dateOfBirth.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập ngày sinh nhật",Toast.LENGTH_SHORT).show();
                return;
            }
            if(username.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập username",Toast.LENGTH_SHORT).show();
                return;
            }

            if (isUpload) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                System.out.println(imageInByte.length);

                MediaManager.get().upload(imageInByte)
                        .option("resource_type", "auto")
                        .option("folder", "AndroidApps/Users/")
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {

                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                user.setImage(resultData.get("secure_url").toString());
                                user.setId(tvId.getText().toString());
                                user.setAddress(address);
                                user.setDateOfBirth(dateOfBirth);
                                user.setEmail(email);
                                user.setFullName(name);
                                user.setMobile(mobile);
                                user.setPosition("ROLE_USER");
                                user.setUsername(username);
                                user.setPassword(tvPassword.getText().toString());
                                user.setStatus(true);
                                if (rb1.isChecked())
                                    user.setSex(true);
                                else user.setSex(false);
                                putData(user);
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        }).dispatch();

            } else {
                user.setImage(tvImg.getText().toString());
                user.setId(tvId.getText().toString());
                user.setAddress(address);
                user.setDateOfBirth(dateOfBirth);
                user.setEmail(email);
                user.setFullName(name);
                user.setMobile(mobile);
                user.setPosition("ROLE_USER");
                user.setUsername(username);
                user.setPassword(tvPassword.getText().toString());
                user.setStatus(true);
                if (rb1.isChecked())
                    user.setSex(true);
                else user.setSex(false);
                putData(user);

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
            Toast.makeText(this,"Test", Toast.LENGTH_LONG).show();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            isUpload = true;
            Uri uri = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);

        }
    }

    private void putData(User user){
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject obj = null;
        try {
            obj = new JSONObject(new Gson().toJson(user));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        System.out.println(obj);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

                Toast.makeText(UserEditProfileActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

                Toast.makeText(UserEditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}