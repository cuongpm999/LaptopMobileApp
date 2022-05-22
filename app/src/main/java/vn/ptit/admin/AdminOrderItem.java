package vn.ptit.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import vn.ptit.R;
import vn.ptit.model.Order;
import vn.ptit.model.User;
import vn.ptit.user.adapter.UserCheckoutAdapter;
import vn.ptit.util.MoneyFormat;

public class AdminOrderItem extends AppCompatActivity {
    private TextView tvFullName, tvAddress, tvMobile, tvStatusOrder, tvDateOrder, tvPaymentType, tvShipment, tvTotalMoney;
    private RecyclerView recyclerView;
    private UserCheckoutAdapter userCheckoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvFullName = findViewById(R.id.tvFullName);
        tvAddress = findViewById(R.id.tvAddress);
        tvMobile = findViewById(R.id.tvMobile);
        tvStatusOrder = findViewById(R.id.tvStatusOrder);
        tvDateOrder = findViewById(R.id.tvDateOrder);
        tvPaymentType = findViewById(R.id.tvPaymentType);
        tvShipment = findViewById(R.id.tvShipment);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
        recyclerView = findViewById(R.id.rvMyCart);

        userCheckoutAdapter = new UserCheckoutAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userCheckoutAdapter);

        Intent intent = getIntent();
        Order order = (Order) intent.getSerializableExtra("order");

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        tvDateOrder.setText("Ngày mua: "+dateFormat.format(order.getCreatedAt()));
        if(order.getPayment().getType_().equalsIgnoreCase("cash"))
            tvPaymentType.setText("Hình thức thanh toán: Thanh toán sau khi giao hàng");
        else if(order.getPayment().getType_().equalsIgnoreCase("digitalWallet"))
            tvPaymentType.setText("Hình thức thanh toán: Thanh toán bằng PayPal");
        else if(order.getPayment().getType_().equalsIgnoreCase("credit"))
            tvPaymentType.setText("Hình thức thanh toán: Thanh toán bằng thẻ tín dụng");
        tvShipment.setText("Đơn vị vận chuyển: "+order.getShipment().getName()+" - "+ MoneyFormat.format(order.getShipment().getPrice()) +" đ");
        tvTotalMoney.setText("Tổng tiền: "+ MoneyFormat.format(order.getPayment().getTotalMoney())+" đ");
        userCheckoutAdapter.setList(order.getCart().getLineItems());
        tvStatusOrder.setText(order.getStatus());

        tvFullName.setText(order.getUser().getFullName());
        tvAddress.setText("Địa chỉ: "+order.getUser().getAddress());
        tvMobile.setText("Điện thoại: "+order.getUser().getMobile());

    }
}