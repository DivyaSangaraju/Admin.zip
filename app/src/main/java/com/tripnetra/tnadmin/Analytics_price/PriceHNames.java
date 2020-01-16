package com.tripnetra.tnadmin.Analytics_price;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tripnetra.tnadmin.Analytics.DCount_citynames;
import com.tripnetra.tnadmin.Analytics.sightseen_name;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceHNames extends AppCompatActivity {

    String sp1value,sp1mnth;
    ArrayList<BarDataSet> yAxis;
    ArrayList<BarEntry> yValues;
    ArrayList<String> xAxis1;
    BarEntry values ;
    BarChart chart;
    BarData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_hnames);

        chart = findViewById(R.id.chart);

        Bundle b = getIntent().getExtras();
        assert b != null;
        sp1value =  b.getString("sp1value");
        Log.i("res",sp1value);
        getdata();
        load_data_from_server();
    }


    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/prasanth/androidphpfiles/adminpanel/PHNames.php", response -> {

              Log.i("sp1valueres",response);

            try {

                RecyclerView recyclerView = findViewById(R.id.DCNamesRV);

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(PriceHNames.this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setHname(json.getString("hotel_name"));
                    adapter.setRcunt(json.getString("hcount"));

                    list.add(adapter);
                }

                recyclerView.setAdapter(new DCount_recycle_adapter(list));

            } catch (JSONException e) {


                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }


        }, error -> {

            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",sp1value);


                Log.i("sp1params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    public class DCount_recycle_adapter  extends RecyclerView.Adapter<DCount_recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        DCount_recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public DCount_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DCount_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cnamesrecycler, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DCount_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.hnameTv.setText(DDataAdapter.getHname());
            vh.rcountTV.setText(DDataAdapter.getRcunt());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,rcountTV;
            Bundle b=new Bundle();

            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);



            }
        }
    }

    public void load_data_from_server() {

        String url = "https://tripnetra.com/prasanth/androidphpfiles/adminpanel/PHNames.php";
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("string",response);

                    try {

                        JSONArray jsonarray = new JSONArray(response);

                        for(int i=0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String score = jsonobject.getString("hcount").trim();
                            String name = jsonobject.getString("hotel_name").trim();

                            Log.i("score",score);
                            xAxis1.add(name);

                            values = new BarEntry(Float.valueOf(score),i);
                            yValues.add(values);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    BarDataSet barDataSet1 = new BarDataSet(yValues, "Hotel Names");
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
                params.put("book_date",sp1value);

                Log.i("barparams", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
