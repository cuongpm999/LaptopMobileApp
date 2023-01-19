package vn.ptit.util;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.cloudinary.android.MediaManager;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

import vn.ptit.config.CloudinaryConfig;

public class MyCustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CloudinaryConfig cloudinaryConfig = new CloudinaryConfig();
        MediaManager.init(this, cloudinaryConfig.getCloudinary());

        CheckoutConfig config = new CheckoutConfig(
                this,
                "ATt_uS_7TlE3LUQ4JBmuA1JkEwv-viD1LZnf8bZBAlZ5mNyTU4ejkt7F0fbq_9d8L-Y98fdOjgcOGLgG",
                Environment.SANDBOX,
                "vn.ptit://paypalpay",
                CurrencyCode.USD,
                UserAction.PAY_NOW, null,
                new SettingsConfig(true, false));
        PayPalCheckout.setConfig(config);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
