package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Daily_bookings2 extends AppCompatActivity {
    String date,Url="https://tripnetra.com/androidphpfiles/extranet/dailynew1.php",FromDate,flag;
    TextView hotel,darshan;
    RecyclerView recyclerView;
    Button b1,b2,b3;
    LinearLayout llyt,llyt1;

    Button CinBtn;
    long mindate;int dateflag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_bookings2);
        date=String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()));
        Log.i("dailybookingdate",date);
        hotel=findViewById(R.id.hcTv);
        recyclerView = findViewById(R.id.recyclerview);
        CinBtn = findViewById(R.id.cinBtn);
        b1 =findViewById(R.id.b1);
        b2 =findViewById(R.id.b2);
        b3 =findViewById(R.id.b3);
        llyt = findViewById(R.id.llyt);
        llyt1 = findViewById(R.id.llyt1);
        darshan=findViewById(R.id.acTv);

        long fdate = System.currentTimeMillis();
        FromDate = Utils.DatetoStr(fdate,0);

        Log.i("FromDate",FromDate);

        CinBtn.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;
            dateflag = 1;
            datedialog();
        });



        b1.setOnClickListener(v -> {
            b1.setBackgroundResource(R.drawable.select);
            b2.setBackgroundResource(R.drawable.boxfill_radius);
            b3.setBackgroundResource(R.drawable.boxfill_radius);
            llyt.setVisibility(View.VISIBLE);
            llyt1.setVisibility(View.GONE);
            flag="1";
            hotels();
            hotelscount();
        });
        b2.setOnClickListener(v -> {
            b2.setBackgroundResource(R.drawable.select);
            b1.setBackgroundResource(R.drawable.boxfill_radius);
            b3.setBackgroundResource(R.drawable.boxfill_radius);
            llyt.setVisibility(View.VISIBLE);
            llyt1.setVisibility(View.GONE);
            flag="2";
            tour();
            tourcount();
        });
        b3.setOnClickListener(v -> {
            b3.setBackgroundResource(R.drawable.select);
            b2.setBackgroundResource(R.drawable.boxfill_radius);
            b1.setBackgroundResource(R.drawable.boxfill_radius);
            flag="3";
            llyt.setVisibility(View.VISIBLE);
            llyt1.setVisibility(View.VISIBLE);
            adultcount();
            darshanam();
            darshancount();
        });

    }


    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Daily_bookings2.this, (view, year, month, day) -> {
            Calendar calndr = Calendar.getInstance();
            calndr.set(year,month,day);

            if (dateflag == 1) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ncal.add(Calendar.DATE, 1);
                FromDate = Utils.DatetoStr(calndr.getTime(),0);
                CinBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                mindate = calndr.getTimeInMillis();
                dateflag = 2;

            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }



    private void hotels() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);
            recyclerView.setVisibility(View.VISIBLE);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("hotel_name"));
                    dataAdapter.setHid("Rooms:"+jobj.getString("bcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                  //  dataAdapter.setMobile(jobj.getString("guest_count"));
                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Daily_bookings2.this));
                recyclerView.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();
                recyclerView.setVisibility(View.GONE);


            }

        }, error -> Toast.makeText(Daily_bookings2.this, "Something went wrong.", Toast.LENGTH_LONG).show())


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","hotel");



                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }
    private void tour() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);

            recyclerView.setVisibility(View.VISIBLE);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setHid("Packages:"+jobj.getString("tcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setMobile("A:"+jobj.getString("adults")+"C:"+jobj.getString("childs"));

                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Daily_bookings2.this));
                recyclerView.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {

                recyclerView.setVisibility(View.GONE);


            }

        }, error -> {



        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","tour");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }
    private void darshanam() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();
            recyclerView.setVisibility(View.VISIBLE);
            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setHid("Packages:"+jobj.getString("darcount"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setMobile("Adults:"+jobj.getString("adults")+"Childs:"+jobj.getString("childs"));
                    list.add(dataAdapter);

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Daily_bookings2.this));
                recyclerView.setAdapter(new hotels_adapter(list));
            } catch (JSONException e) {
                e.printStackTrace();
                recyclerView.setVisibility(View.GONE);


            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","darshan");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }

    private void hotelscount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("bcount1");
                    hotel.setText(Daily_bookings2.this.getResources().getString(R.string.hotel)+" -- "+name);

                    Log.i("name1", String.valueOf(name));

                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","hotel");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }

    private void tourcount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {

            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("tcount1");
                    hotel.setText(Daily_bookings2.this.getResources().getString(R.string.tour1)+" -- "+name);

                    Log.i("name1", String.valueOf(name));


                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("date",FromDate);
                params.put("category","tour");
                params.put("type","notsingle");

                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }

    private void darshancount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("darcount1");
                    hotel.setText(Daily_bookings2.this.getResources().getString(R.string.darshan1)+" -- "+name);
                    Log.i("name1", String.valueOf(name));


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

            Toast.makeText(Daily_bookings2.this, "Something went wrong Try Again.", Toast.LENGTH_LONG).show();
        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","darshan");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
        requestQueue.add(stringRequest);
    }


    private void adultcount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    if(flag.equals("3")){

                    String named = jsonobject.getString("adcount");
                    darshan.setText("Adults--"+named);
                    Log.i("named", String.valueOf(named));
                 //   Toast.makeText(this, named, Toast.LENGTH_SHORT).show();
                    } else{

                       darshan.setText("");
                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            Toast.makeText(Daily_bookings2.this, "Something went wrong Try Again.", Toast.LENGTH_LONG).show();
        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","darshancount");

                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Daily_bookings2.this));
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

            TextView hotelnameTv,hotelcountTv,supportTv,PassTv;
            ViewHolder(View iv) {

                super(iv);

                hotelnameTv = iv.findViewById(R.id.hotelnameTv);
                hotelcountTv = iv.findViewById(R.id.hotelcountTv);
                supportTv = iv.findViewById(R.id.SupTv);
                PassTv = iv.findViewById(R.id.passTv);
                Context context = iv.getContext();



            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);
            vh.hotelnameTv.setText(dataAdap.getHname());
            vh.hotelcountTv.setText(dataAdap.getHid());
            vh.supportTv.setText(dataAdap.getGname());
            vh.PassTv.setText(dataAdap.getMobile());


        }

        @Override
        public int getItemCount() { return listadapter.size(); }
    }


}
