package vn.ptit.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.ptit.R;
import vn.ptit.admin.AdminEditManufacturer;
import vn.ptit.model.ChartReport;
import vn.ptit.model.Manufacturer;
import vn.ptit.util.Encoding;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminStatAdapter extends RecyclerView.Adapter<AdminStatAdapter.AdminManufacturerViewHolder> {
    private List<String> mLabel;
    private List<String> mData;
    private Context context;

    public AdminStatAdapter(Context context) {
        this.context = context;
        mLabel = new ArrayList<>();
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public AdminManufacturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_stat, parent, false);
        return new AdminManufacturerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminManufacturerViewHolder holder, int position) {
        holder.tv1.setText(mLabel.get(position));
        holder.tv2.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mLabel.size();
    }

    public class AdminManufacturerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;

        public AdminManufacturerViewHolder(@NonNull View view) {
            super(view);
            tv1 = view.findViewById(R.id.tvNameStat);
            tv2 = view.findViewById(R.id.tvDataStat);
        }
    }

    public void getData(String url) {
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mData.clear();
                mLabel.clear();

                ChartReport chartReport = new Gson().fromJson(Encoding.fixEncoding(response), ChartReport.class);
                mLabel = Arrays.asList(chartReport.getLabel());
                if(url.contains("user-total-money")) {
                    for (int i = 0; i < chartReport.getData().length; i++) {

                        mData.add(MoneyFormat.format(chartReport.getData()[i]) + " đ");
                    }
                }
                if(url.contains("income-last-5-month")) {
                    for (int i = 0; i < chartReport.getData().length; i++) {

                        mData.add(MoneyFormat.format(chartReport.getData()[i]) + " đ");
                    }
                }

                if(url.contains("item-best-seller")) {
                    for (int i = 0; i < chartReport.getData().length; i++) {

                        mData.add(String.format("%.0f",chartReport.getData()[i]));
                    }
                }
                notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

}
