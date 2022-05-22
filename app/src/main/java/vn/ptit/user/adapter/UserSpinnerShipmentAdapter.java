package vn.ptit.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import vn.ptit.model.Shipment;
import vn.ptit.user.fragment.UserFragmentCheckout;
import vn.ptit.util.Encoding;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class UserSpinnerShipmentAdapter extends BaseAdapter {
    private List<Shipment> mList;
    private Context context;
    public UserSpinnerShipmentAdapter(Context context) {
        this.mList = new ArrayList<>();
        this.context = context;
        getData();

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

    public Shipment getShipment(int i) {
        return mList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = LayoutInflater.from(context).inflate(R.layout.user_spinner_item_shipment, viewGroup, false);
        TextView tv1 = row.findViewById(R.id.tvNameShipmentSpinner);
        TextView tv2 = row.findViewById(R.id.tvPriceShipmentSpinner);
        tv1.setText(mList.get(i).getName());
        tv2.setText(MoneyFormat.format(mList.get(i).getPrice())+" đ");
        return row;
    }

    public void getData() {
        String url = MyDomainService.name +"/api/shipment/find-all";
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Shipment shipment = new Gson().fromJson(String.valueOf(response.getJSONObject(i)), Shipment.class);
                        mList.add(shipment);

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

}
