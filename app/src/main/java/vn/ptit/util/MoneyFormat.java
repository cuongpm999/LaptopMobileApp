package vn.ptit.util;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormat {
    public static String format(double price){
        Locale local = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getInstance(local);
        return numberFormat.format(price);
    }
}
