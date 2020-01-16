package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourNames extends AppCompatActivity {
    RecyclerView recyclerView;
    String pos,SDate,EDate,ctname,rcount;
    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;
    BarData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_names);
        recyclerView = findViewById(R.id.recyclerviews);
        chart = findViewById(R.id.chart);

        Bundle bb = getIntent().getExtras();
        assert bb!=null;
        pos = bb.getString("postn");
        SDate = bb.getString("FromDate");
        EDate = bb.getString("ToDate");
        ctname = bb.getString("search_city");
        rcount = bb.getString("cnt");
        getpackagedetails();
        load_data_from_server();
    }
    public void load_data_from_server() {

        String url = "https://tripnetra.com/androidphpfiles/adminpanel/gettournames.php";
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,response -> {
                    Log.d("string",response);

                    try {

                        JSONArray jsonarray = new JSONArray(response);

                        for(int i=0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String score = jsonobject.getString("scount").trim();
                            String name = jsonobject.getString("sightseen_name").trim();

                            Log.i("sssss",score);
                            xAxis1.add(name);

                            values = new BarEntry(Float.valueOf(score),i);
                            yValues.add(values);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }


                    BarDataSet barDataSet1 = new BarDataSet(yValues, "Tour names");
                    barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSet1.setBarSpacePercent(150f);

                    yAxis = new ArrayList<>();
                    yAxis.add(barDataSet1);
                    String names[]= xAxis1.toArray(new String[xAxis1.size()]);
                    data = new BarData(names,yAxis);
                    chart.setData(data);
                    chart.setDescription("");
                    chart.animateXY(2000, 2000);
                    chart.invalidate();

                },
                error -> {
                    if(error != null){

                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",pos);
                params.put("cname",ctname);


                if(pos.equals("0")){
                    params.put("travel_sdate", SDate);
                    params.put("travel_edate", EDate);

                } else{
                    params.put("ds_date",SDate);
                    params.put("de_date",EDate);
                }
                Log.i("pa", String.valueOf(params));
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void getpackagedetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/gettournames.php", response -> {

            List<DataAdapter> list = new ArrayList<>();

            try {
                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setHid(jobj.getString("scount"));
                    list.add(dataAdapter);

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(TourNames.this));
                recyclerView.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

        },error -> { Toast.makeText(getApplicationContext(),"something went wrong Try again",Toast.LENGTH_SHORT).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",pos);
                params.put("cname",ctname);

                if(pos.equals("0")){
                    params.put("travel_sdate", SDate);
                    params.put("travel_edate", EDate);

                } else{
                    params.put("ds_date",SDate);
                    params.put("de_date",EDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }


        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

        private List<DataAdapter> listadapter;

        hotels_adapter(List<DataAdapter> listAdapter) {
            super();
            this.listadapter = listAdapter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotels_display, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hotelnameTv,hotelcountTv;
            Bundle b=new Bundle();
            ViewHolder(View iv) {
                super(iv);
                hotelnameTv = iv.findViewById(R.id.hotelnameTv);
                hotelcountTv = iv.findViewById(R.id.hotelcountTv);

            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);
            vh.hotelnameTv.setText(dataAdap.getHname());
            vh.hotelcountTv.setText(dataAdap.getHid());

        }
        @Override
        public int getItemCount() { return listadapter.size(); }
    }


}