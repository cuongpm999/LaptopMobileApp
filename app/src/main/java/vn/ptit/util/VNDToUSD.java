package vn.ptit.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class VNDToUSD {
    public static double getTyGia() {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(
                    "https://free.currconv.com/api/v7/convert?apiKey=73186a7f4f40e1abc58c&q=VND_USD&compact=y");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = new Gson().fromJson(content.toString(), JsonObject.class);
        JsonObject object1 = jsonObject.get("VND_USD").getAsJsonObject();
        double tyGia = object1.get("val").getAsDouble();
        return tyGia;
    }
}
