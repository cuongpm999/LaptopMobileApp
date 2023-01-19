package vn.ptit.admin.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import vn.ptit.R;
import vn.ptit.model.ChartReport;
import vn.ptit.util.Encoding;
import vn.ptit.util.MoneyFormat;
import vn.ptit.util.MyDomainService;
import vn.ptit.util.VolleySingleton;

public class AdminFragmentHome extends Fragment {
    private BarChart barChart;
    private PieChart pieChart,pieChartUser;
    private TextView txtTotalMoney, txtTotalUser, txtTotalItem;
    private Context context;

    public AdminFragmentHome(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        pieChartUser = view.findViewById(R.id.pieChartUser);

        txtTotalItem = view.findViewById(R.id.txtTotalItem);
        txtTotalUser = view.findViewById(R.id.txtTotalUser);
        txtTotalMoney = view.findViewById(R.id.txtTotalMoney);

        getDataBarChart();
        getDataPieChart();
        getDataPieChartUser();

        getDataTotalMoney();
        getDataTotalOrder();
        getDataTotalUser();


    }

    private void getDataBarChart() {


        String url = MyDomainService.name + "/api/statistic/income-last-5-month";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ChartReport chartReport = new Gson().fromJson(Encoding.fixEncoding(response), ChartReport.class);

                List datas = new ArrayList();
                datas.add(new BarEntry(0f, chartReport.getData()[0]));
                datas.add(new BarEntry(1f, chartReport.getData()[1]));
                datas.add(new BarEntry(2f, chartReport.getData()[2]));
                datas.add(new BarEntry(3f, chartReport.getData()[3]));
                datas.add(new BarEntry(4f, chartReport.getData()[4]));

                BarDataSet barDataSet = new BarDataSet(datas, "Thống kê thu nhập 5 tháng gần nhất");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                barChart.getDescription().setEnabled(true);
                barChart.animateY(2000);
                barChart.setFitBars(true);

                XAxis xAxis = barChart.getXAxis();
                String[] labels = new String[]{chartReport.getLabel()[0], chartReport.getLabel()[1], chartReport.getLabel()[2], chartReport.getLabel()[3], chartReport.getLabel()[4]};
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    private void getDataPieChart() {


        String url = MyDomainService.name + "/api/statistic/item-best-seller";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ChartReport chartReport = new Gson().fromJson(Encoding.fixEncoding(response), ChartReport.class);

                List datas = new ArrayList();
                datas.add(new PieEntry(chartReport.getData()[0],chartReport.getLabel()[0]));
                datas.add(new PieEntry(chartReport.getData()[1],chartReport.getLabel()[1]));
                datas.add(new PieEntry(chartReport.getData()[2],chartReport.getLabel()[2]));
                datas.add(new PieEntry(chartReport.getData()[3],chartReport.getLabel()[3]));
                datas.add(new PieEntry(chartReport.getData()[4],chartReport.getLabel()[4]));

                PieDataSet pieDataSet = new PieDataSet(datas, "Thống kê 5 sản phẩm bán chạy nhất");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.animateXY(2000,2000);

                String[] labels = new String[]{chartReport.getLabel()[0], chartReport.getLabel()[1], chartReport.getLabel()[2], chartReport.getLabel()[3], chartReport.getLabel()[4]};
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    private void getDataPieChartUser() {

        String url = MyDomainService.name + "/api/statistic/user-total-money";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ChartReport chartReport = new Gson().fromJson(Encoding.fixEncoding(response), ChartReport.class);

                List datas = new ArrayList();
                datas.add(new PieEntry(chartReport.getData()[0],chartReport.getLabel()[0]));
                datas.add(new PieEntry(chartReport.getData()[1],chartReport.getLabel()[1]));
                datas.add(new PieEntry(chartReport.getData()[2],chartReport.getLabel()[2]));
                datas.add(new PieEntry(chartReport.getData()[3],chartReport.getLabel()[3]));
                datas.add(new PieEntry(chartReport.getData()[4],chartReport.getLabel()[4]));

                PieDataSet pieDataSet = new PieDataSet(datas, "Thống kê 5 khách hàng mua nhiều nhất");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);
                pieChartUser.setData(pieData);
                pieChartUser.animateXY(2000,2000);

                String[] labels = new String[]{chartReport.getLabel()[0], chartReport.getLabel()[1], chartReport.getLabel()[2], chartReport.getLabel()[3], chartReport.getLabel()[4]};
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    private void getDataTotalMoney() {

        String url = MyDomainService.name + "/api/statistic/total-money";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                double totalMoney = Double.parseDouble(response);
                txtTotalMoney.setText(MoneyFormat.format(totalMoney)+" đ");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    private void getDataTotalOrder() {

        String url = MyDomainService.name + "/api/statistic/total-order";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int totalOrder = Integer.parseInt(response);
                txtTotalItem.setText(totalOrder+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    private void getDataTotalUser() {

        String url = MyDomainService.name + "/api/statistic/total-user";

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int totalUser = Integer.parseInt(response);
                txtTotalUser.setText(totalUser+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }
}
