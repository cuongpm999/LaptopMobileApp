package vn.ptit.user.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paypal.checkout.paymentbutton.PaymentButton;
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatus;
import com.paypal.checkout.paymentbutton.PaymentButtonEligibilityStatusChanged;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import vn.ptit.R;

public class UserFragmentDigitalWallet extends Fragment {
    private PaymentButton payPalButton;

    public UserFragmentDigitalWallet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment_digital_wallet,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        payPalButton = view.findViewById(R.id.payPalButton);

        payPalButton.setPaymentButtonEligibilityStatusChanged(new PaymentButtonEligibilityStatusChanged() {
            @Override
            public void onPaymentButtonEligibilityStatusChanged(@NotNull PaymentButtonEligibilityStatus paymentButtonEligibilityStatus) {
                payPalButton.setVisibility(View.GONE);
            }
        });
    }

    public PaymentButton getPayPalButton() {
        return payPalButton;
    }
}
