package vn.ptit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.cloudinary.android.MediaManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import vn.ptit.admin.AdminActivity;
import vn.ptit.admin.AdminAddLaptop;
import vn.ptit.config.CloudinaryConfig;
import vn.ptit.model.Laptop;
import vn.ptit.model.User;
import vn.ptit.user.UserActivity;
import vn.ptit.util.Encoding;
import vn.ptit.util.MyDomainService;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername, etPassword;
    private Button button;
    private TextView tvRegister;
    private SharedPreferences sharedPreferences;
    private ImageView ivGoogle, ivFacebook;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 345232167;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        button = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        ivGoogle = findViewById(R.id.ivGoogle);
        ivGoogle.setOnClickListener(this);
        ivFacebook = findViewById(R.id.ivFace);
        ivFacebook.setOnClickListener(this);

        button.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String token = task.getResult();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.commit();
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        sharedPreferences = getSharedPreferences("dataApp", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createRequest();

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        System.out.println(object.toString());
                                        User user = new User();

                                        try {
                                            user.setUsername(object.getString("id"));
                                            user.setFullName(object.getString("name"));
                                            user.setEmail(object.getString("email"));
                                            user.setImage(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        checkUserFacebook(user);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(exception.getMessage().toString());
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == button) {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if(username.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập username",Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.isEmpty()){
                Toast.makeText(this,"Bạn hãy nhập password",Toast.LENGTH_SHORT).show();
                return;
            }

            String url = MyDomainService.name + "/api/user/check-login?username=" + username + "&password=" + password;

            ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();

                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    if (response.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Bạn đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new Gson().fromJson(response, User.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("usernameLogin", username);
                        editor.commit();
                        if (user.getPosition().equalsIgnoreCase("ROLE_USER")) {
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            startActivity(intent);
                        }
                        if (user.getPosition().equalsIgnoreCase("ROLE_ADMIN")) {
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
        }

        if (view == tvRegister) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        if (view == ivGoogle) {
            signIn();
        }

        if (view == ivFacebook) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        }
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            checkUserGoogle(user.getEmail());

                        } else {
                            Toast.makeText(MainActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void insertUser(User user,ProgressDialog pDialog) {
        String url = MyDomainService.name + "/api/user/insert";
        user.setAddress("N/A");
        user.setDateOfBirth("2000-01-06T00:00:00.000+00:00");
        user.setMobile("N/A");
        user.setPosition("ROLE_USER");
        user.setPassword("N/A");
        user.setSex(true);

        JSONObject obj = null;
        try {
            obj = new JSONObject(new Gson().toJson(user));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        System.out.println(obj);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void checkUserGoogle(String email) {
        String url = MyDomainService.name + "/api/user/find-by-email/" + email;

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if (response.isEmpty()) {
                    User user = new User();

                    user.setEmail(mAuth.getCurrentUser().getEmail());
                    user.setFullName(mAuth.getCurrentUser().getDisplayName());
                    user.setImage(mAuth.getCurrentUser().getPhotoUrl().toString());
                    user.setUsername(mAuth.getCurrentUser().getUid());

                    insertUser(user,pDialog);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usernameLogin", mAuth.getCurrentUser().getUid());
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                } else {
                    User user = new Gson().fromJson(Encoding.fixEncoding(response), User.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usernameLogin", user.getUsername());
                    editor.commit();

                    pDialog.hide();

                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void checkUserFacebook(User user) {
        String url = MyDomainService.name + "/api/user/find-by-email/" + user.getEmail();

        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if (response.isEmpty()) {

                    insertUser(user,pDialog);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usernameLogin", user.getUsername());
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                } else {
                    User user = new Gson().fromJson(Encoding.fixEncoding(response), User.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usernameLogin", user.getUsername());
                    editor.commit();

                    pDialog.hide();

                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

}