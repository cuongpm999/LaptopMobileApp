package vn.ptit.config;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryConfig {
    public Map<String,String> getCloudinary() {
        Map<String,String> config = new HashMap();
        config.put("cloud_name", "cuongpham");
        config.put("api_key", "563118948926745");
        config.put("api_secret", "BXj88MsN5XQSBj8Y-CA5qUTVqr8");
        return config;
    }
}
