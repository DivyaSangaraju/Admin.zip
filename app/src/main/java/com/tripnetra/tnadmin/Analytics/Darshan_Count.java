package com.tripnetra.tnadmin.Analytics;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Darshan_Count extends AppCompatActivity {

    Button CinBtn,CoutBtn,Bkng,Trvl;
    TextView HCount,ACoun;
    String position;
    LinearLayout LLTHCount,linear;
    String FromDate,ToDate,link="https://tripnetra.com/androidphpfiles/adminpanel/darshanadult.php";
    long mindate;int dateflag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darshan_c);
        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);
        LLTHCount = findViewById(R.id.HotelcountLLt);
        linear = findViewById(R.id.lineardyna);
        Bkng = findViewById(R.id.booking);
        Trvl = findViewById(R.id.travel);
        HCount = findViewById(R.id.hotelcountTV);
        ACoun = findViewById(R.id.AdultsTv);
        long fdate = System.currentTimeMillis(), tdate = System.currentTimeMillis();
        FromDate = Utils.DatetoStr(fdate,0);
        ToDate = Utils.DatetoStr(tdate,0);
        CinBtn.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;
            dateflag = 1;
            datedialog();
        });

        CoutBtn.setOnClickListener(v -> {
            mindate = fdate;
            dateflag = 2;
            datedialog();

        });



        Bkng.setOnClickListener(view -> {
            getdarshancount();
            position=String.valueOf(0);
            Bkng.setBackgroundResource(R.drawable.select);
            Trvl.setBackgroundResource(R.drawable.boxfill_radius);
            ACoun.setVisibility(View.VISIBLE);
          //  ACoun.setEnabled(false);

        });

        Trvl.setOnClickListener(view -> {
            getdarshancount();
            position=String.valueOf(1);
            Trvl.setBackgroundResource(R.drawable.select);
            Bkng.setBackgroundResource(R.drawable.boxfill_radius);
            ACoun.setVisibility(View.GONE);

        });

        HCount.setOnClickListener(v -> {
            Intent intent = new Intent(Darshan_Count.this,DCount_citynames.class);
            Bundle bundle = new Bundle();
            bundle.putString("position",position);
            bundle.putString("FromDate",FromDate);
            bundle.putString("ToDate",ToDate);
            intent.putExtras(bundle);
            startActivity(intent);
            Log.i("intent", String.valueOf(bundle));
        });




        ACoun.setOnClickListener(view -> {
            Intent tent=new Intent(Darshan_Count.this,Adult_count.class);
            Bundle bundle = new Bundle();
            bundle.putString("FromDate",FromDate);
            bundle.putString("ToDate",ToDate);
            tent.putExtras(bundle);
            startActivity(tent);
        });
    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Darshan_Count.this, (view, year, month, day) -> {
            Calendar calndr = Calendar.getInstance();
            calndr.set(year,month,day);

            if (dateflag == 1) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ncal.add(Calendar.DATE, 1);
                FromDate = Utils.DatetoStr(calndr.getTime(),0);
                ToDate = Utils.DatetoStr(ncal.getTime(),0);
                CinBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                CoutBtn.setText(Utils.DatetoStr(ncal.getTime(),1));
                mindate = calndr.getTimeInMillis();
                dateflag = 2;
                datedialog();

            } else if (dateflag == 2) {

                ToDate = Utils.DatetoStr(calndr.getTime(),0);
                CoutBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                getAdultcount();

            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

           pickerDialog.show();

    }

    private void getAdultcount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,link, response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("acount");

                    if(!name.equals("")){

                       // ACoun.setVisibility(View.GONE);
                        ACoun.setText("Adults"+"\n"+name);
                       // ACoun.setEnabled(false);

                    } else{

                        Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show();
                    }

                    Log.i("name", String.valueOf(name));

                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }, error -> {

            Toast.makeText(Darshan_Count.this, "Something went wrong Try again.", Toast.LENGTH_LONG).show();

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                    params.put("category","count");
                    params.put("start_date", FromDate);
                    params.put("end_date", ToDate);

                Log.i("india", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Darshan_Count.this));
        requestQueue.add(stringRequest);
    }

    private void getdarshancount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/gettourscount.php", response -> {

            if(!response.equals("")){
                HCount.setText("Darshan"+"\n"+response);
                HCount.setVisibility(View.VISIBLE);

            } else {
                HCount.setText("0");
                HCount.setVisibility(View.VISIBLE);
            }

        }, error -> {

            Toast.makeText(Darshan_Count.this, "Something went wrong Try again.", Toast.LENGTH_LONG).show();

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",position);
                params.put("category","darshan");

                if(position.equals("0")){
                    params.put("start_date", FromDate);
                    params.put("end_date", ToDate);

                } else{
                    params.put("travel_sdate",FromDate);
                    params.put("travel_edate",ToDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Darshan_Count.this));
        requestQueue.add(stringRequest);
    }


}
