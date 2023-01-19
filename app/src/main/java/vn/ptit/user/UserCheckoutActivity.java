package vn.ptit.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import vn.ptit.R;
import vn.ptit.user.fragment.UserFragmentCheckout;

public class UserCheckoutActivity extends AppCompatActivity {
    private TextView tvTotalMoneyCheckout, tvBtnCheckout;
    private UserFragmentCheckout userFragmentCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTotalMoneyCheckout = findViewById(R.id.tvTotalMoneyCheckout);
        tvBtnCheckout = findViewById(R.id.tvBtnCheckout);

        userFragmentCheckout = new UserFragmentCheckout(tvTotalMoneyCheckout);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragCheckout,userFragmentCheckout);
        transaction.commit();

        tvBtnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFragmentCheckout.setOrder();
            }
        });

    }
}