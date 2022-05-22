package vn.ptit.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import vn.ptit.MainActivity;
import vn.ptit.R;
import vn.ptit.admin.AdminActivity;
import vn.ptit.model.User;
import vn.ptit.user.ChangePasswordActivity;
import vn.ptit.user.UserActivity;
import vn.ptit.user.UserEditProfileActivity;
import vn.ptit.user.UserMyOrderActivity;
import vn.ptit.util.Encoding;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserFragmentMe extends Fragment implements View.OnClickListener{
    private ImageView imageView;
    private TextView tvUsername;
    private SharedPreferences sharedPreferences;
    private Context context;
    private TextView tvMyOrder,tvLogout, tvEditProfile, tvChangePassword;
    private User user;

    public UserFragmentMe(SharedPreferences sharedPreferences, Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.userAvatar);
        tvUsername = view.findViewById(R.id.userUsername);
        tvMyOrder = view.findViewById(R.id.tvMyOrder);
        tvLogout = view.findViewById(R.id.tvLogout);
        tvMyOrder.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvEditProfile.setOnClickListener(this);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(this);

        getData();

    }

    @Override
    public void onClick(View view) {
        if(view == tvMyOrder){
            Intent intent = new Intent(getContext(), UserMyOrderActivity.class);
            startActivity(intent);
        }

        if(view == tvLogout){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("usernameLogin");
            editor.commit();

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        if(view == tvEditProfile){
            Intent intent = new Intent(getContext(), UserEditProfileActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }

        if(view == tvChangePassword){
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    }

    private void getData(){
        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/user/find-by-username/" + username;

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(context, Encoding.fixEncoding(response), Toast.LENGTH_SHORT).show();
                user = new Gson().fromJson(Encoding.fixEncoding(response), User.class);

                Glide.with(context)
                        .load(user.getImage())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(imageView);

                tvUsername.setText(user.getFullName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
