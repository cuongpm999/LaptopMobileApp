package vn.ptit.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditLaptop;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminSpinnerManufacturerAdapter extends BaseAdapter {
    private List<Manufacturer> mList;
    private Context context;

    public AdminSpinnerManufacturerAdapter(Context context, boolean isEdit) {
        this.mList = new ArrayList<>();
        this.context = context;
        if(isEdit) getDataToEdit();
        else getData();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public Manufacturer getManufacturer(int i) {
        return mList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = LayoutInflater.from(context).inflate(R.layout.admin_spinner_item_manufacturer, viewGroup, false);
        TextView tv1 = row.findViewById(R.id.tvNameManufacturerSpinner);
        TextView tv2 = row.findViewById(R.id.tvAddressManufacturerSpinner);
        tv1.setText(mList.get(i).getName());
        tv2.setText(mList.get(i).getAddress());
        return row;
    }

    public void getData() {
        String url = MyDomainService.name +"/api/manufacturer/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Manufacturer manufacturer = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Manufacturer.class);
                        mList.add(manufacturer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getDataToEdit() {
        String url = MyDomainService.name +"/api/manufacturer/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Manufacturer manufacturer = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Manufacturer.class);
                        mList.add(manufacturer);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();

                AdminEditLaptop adminEditLaptop = (AdminEditLaptop) context;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getId().equals(adminEditLaptop.getIdManufacturer())) {
                        adminEditLaptop.getSpinner().setSelection(i);
                        break;
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
