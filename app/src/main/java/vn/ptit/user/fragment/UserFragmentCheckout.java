package vn.ptit.user.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.cancel.OnCancel;
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
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.ptit.R;
import vn.ptit.model.Cart;
import vn.ptit.model.Cash;
import vn.ptit.model.Credit;
import vn.ptit.model.DigitalWallet;
import vn.ptit.model.Order;
import vn.ptit.model.Shipment;
import vn.ptit.model.User;
import vn.ptit.user.UserActivity;
import vn.ptit.user.adapter.UserCheckoutAdapter;
import vn.ptit.user.adapter.UserSpinnerShipmentAdapter;
import vn.ptit.user.adapter.UserViewPagerPaymentAdapter;
import vn.ptit.util.Encoding;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserFragmentCheckout extends Fragment {
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private UserCheckoutAdapter userCheckoutAdapter;
    private TextView tvFullName, tvMobile, tvAddress;
    private Spinner spinner;
    private UserSpinnerShipmentAdapter userSpinnerShipmentAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private UserViewPagerPaymentAdapter userViewPagerPaymentAdapter;
    private TextView tvTotalAmount, tvPriceShipment, tvTotalMoney, tvTotalMoneyCheckout;

    private double totalAmountCart;
    private String paymentType = "cash";
    private double totalMoney = 0;
    private Cart cart;
    private User user;
    private Shipment shipment;

    private PaymentButton payPalButton;
    private EditText etNumber, etType, etDate;

    public UserFragmentCheckout(TextView tvTotalMoneyCheckout) {
        this.tvTotalMoneyCheckout = tvTotalMoneyCheckout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_checkout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvPriceShipment = view.findViewById(R.id.tvPriceShipment);
        tvTotalMoney = view.findViewById(R.id.tvTotalMoney);

        sharedPreferences = getContext().getSharedPreferences("dataApp", MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.rvUserCheckout);
        userCheckoutAdapter = new UserCheckoutAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userCheckoutAdapter);
        getData();

        tvFullName = view.findViewById(R.id.tvFullName);
        tvMobile = view.findViewById(R.id.tvMobile);
        tvAddress = view.findViewById(R.id.tvAddress);

        getProfile();

        spinner = view.findViewById(R.id.spinnerShipment);
        userSpinnerShipmentAdapter = new UserSpinnerShipmentAdapter(getContext());
        spinner.setAdapter(userSpinnerShipmentAdapter);

        userViewPagerPaymentAdapter = new UserViewPagerPaymentAdapter(getChildFragmentManager(), 3);
        tabLayout = view.findViewById(R.id.tabLayoutPayment);
        viewPager = view.findViewById(R.id.viewPagerPayment);
        viewPager.setAdapter(userViewPagerPaymentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shipment = userSpinnerShipmentAdapter.getShipment(i);
                tvPriceShipment.setText(MoneyFormat.format(userSpinnerShipmentAdapter.getShipment(i).getPrice()) + " đ");
                tvTotalMoney.setText(MoneyFormat.format((totalAmountCart + userSpinnerShipmentAdapter.getShipment(i).getPrice())) + " đ");
                tvTotalMoneyCheckout.setText(MoneyFormat.format((totalAmountCart + userSpinnerShipmentAdapter.getShipment(i).getPrice())) + " đ");
                totalMoney = totalAmountCart + userSpinnerShipmentAdapter.getShipment(i).getPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        paymentType = "cash";
                        break;
                    case 1:
                        paymentType = "digitalWallet";
                        break;
                    case 2:
                        paymentType = "credit";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void getData() {
        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/cart/find-current?username=" + username;
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cart = new Gson().fromJson(response, Cart.class);
                userCheckoutAdapter.setList(cart.getLineItems());
                tvTotalAmount.setText(MoneyFormat.format(cart.getTotalAmount()) + " đ");
                totalAmountCart = cart.getTotalAmount();
                setDataCheckout();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getProfile() {
        String username = sharedPreferences.getString("usernameLogin", "");
        String url = MyDomainService.name + "/api/user/find-by-username/" + username;

        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user = new Gson().fromJson(Encoding.fixEncoding(response), User.class);

                tvFullName.setText(user.getFullName());
                tvMobile.setText(user.getMobile());
                tvAddress.setText(user.getAddress());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void setDataCheckout() {
        String url = MyDomainService.name + "/api/shipment/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Shipment shipment = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Shipment.class);
                        tvPriceShipment.setText(MoneyFormat.format(shipment.getPrice()) + " đ");
                        tvTotalMoney.setText(MoneyFormat.format((totalAmountCart + shipment.getPrice())) + " đ");
                        tvTotalMoneyCheckout.setText(MoneyFormat.format((totalAmountCart + shipment.getPrice())) + " đ");
                        totalMoney = totalAmountCart + shipment.getPrice();
                        break;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void setOrder() {
        Order order = new Order();
        if (paymentType.equalsIgnoreCase("cash")) {
            Cash cash = new Cash();
            cash.setTotalMoney(totalMoney);
            cash.setCashTendered(totalMoney);
            cash.setType_(paymentType);
            order.setPayment(cash);

            order.setCart(cart);
            order.setShipment(shipment);
            order.setUser(user);
            sendOrder(order);
        }
        if (paymentType.equalsIgnoreCase("credit")) {
            etNumber = ((UserFragmentCredit) userViewPagerPaymentAdapter.getFragmentList().get(2)).getEtNumber();
            etDate = ((UserFragmentCredit) userViewPagerPaymentAdapter.getFragmentList().get(2)).getEtDate();
            etType = ((UserFragmentCredit) userViewPagerPaymentAdapter.getFragmentList().get(2)).getEtType();

            if (etNumber.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Bạn hãy nhập số tài khoản", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etType.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Bạn hãy nhập loại thẻ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etDate.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Bạn hãy nhập ngày hết hạn", Toast.LENGTH_SHORT).show();
                return;
            }

            Credit credit = new Credit();
            credit.setTotalMoney(totalMoney);
            credit.setType(etType.getText().toString());
            credit.setDate(etDate.getText().toString());
            credit.setNumber(etNumber.getText().toString());
            credit.setType_(paymentType);
            order.setPayment(credit);

            order.setCart(cart);
            order.setShipment(shipment);
            order.setUser(user);
            sendOrder(order);
        }
        if (paymentType.equalsIgnoreCase("digitalWallet")) {
            DigitalWallet digitalWallet = new DigitalWallet();
            digitalWallet.setTotalMoney(totalMoney);
            digitalWallet.setName("Pay with Paypal");
            digitalWallet.setType_(paymentType);
            order.setPayment(digitalWallet);

            payPalButton = ((UserFragmentDigitalWallet) userViewPagerPaymentAdapter.getFragmentList().get(1)).getPayPalButton();

            payPalButton.setup(
                    new CreateOrder() {
                        @Override
                        public void create(@NotNull CreateOrderActions createOrderActions) {
                            ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                            double pricePaypal = totalMoney / 23000;
                            NumberFormat formatter = new DecimalFormat("#0");
                            purchaseUnits.add(
                                    new PurchaseUnit.Builder()
                                            .amount(
                                                    new Amount.Builder()
                                                            .currencyCode(CurrencyCode.USD)
                                                            .value(formatter.format(pricePaypal))
                                                            .build()
                                            )
                                            .build()
                            );
                            com.paypal.checkout.order.Order order = new com.paypal.checkout.order.Order(
                                    OrderIntent.CAPTURE,
                                    new AppContext.Builder()
                                            .userAction(UserAction.PAY_NOW)
                                            .build(),
                                    purchaseUnits, null
                            );
                            createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        }
                    },
                    new OnApprove() {
                        @Override
                        public void onApprove(@NotNull Approval approval) {
                            approval.getOrderActions().capture(new OnCaptureComplete() {
                                @Override
                                public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                    order.setCart(cart);
                                    order.setShipment(shipment);
                                    order.setUser(user);
                                    sendOrder(order);
                                }
                            });
                        }
                    },
                    null,
                    new OnCancel() {
                        @Override
                        public void onCancel() {
                            Log.d("OnCancel", "Buyer cancelled the PayPal experience.");
                            Toast.makeText(getContext(), "Buyer cancelled the PayPal experience", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new OnError() {
                        @Override
                        public void onError(@NotNull ErrorInfo errorInfo) {
                            Log.d("OnError", String.format("Error: %s", errorInfo));
                        }
                    }

            );

            payPalButton.performClick();
        }

    }

    private void sendOrder(Order order) {
        ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        String token = sharedPreferences.getString("token", "");
        String url = MyDomainService.name + "/api/order/insert?token=" + token;
        JSONObject obj = null;
        try {
            obj = new JSONObject(new Gson().toJson(order));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        System.out.println(obj);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

//                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UserActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }


}
