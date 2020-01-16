package com.tripnetra.tnadmin.Analytics_price;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.tripnetra.tnadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ComparisonActivity extends AppCompatActivity {

    static final String[] Months = new String[] { "aaa","January", "February",
    "March", "April", "May", "June", "July", "August", "September",
    "October", "November", "December" };
    String value,posss,store,inc,valueyear,store1,value2,posss2,store2,inc2,
    value3,posss3,store3,inc3,name,name1,name3,x1,x2,x3,n1,n2,n3,hh,nn,hh1,yy,yy1,hh2,yy2;
    boolean pause = false;
    TextView Hrange,Hrange2,yrange,Hrange3;
    LinearLayout lyt1,lyt2,lyt3,Tlyt;
   // Button getgraphs;
    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        Hrange = findViewById(R.id.rrangeTv);
        Hrange2 = findViewById(R.id.rrange2Tv);
        Hrange3 = findViewById(R.id.rrange3Tv);
        yrange = findViewById(R.id.yrangeTv);
        Button btn1 =findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.plusbtn);
      //  getgraphs = findViewById(R.id.getgraphsbtn);
        lyt1 = findViewById(R.id.lyt1);
        lyt2 = findViewById(R.id.lyt2);
        lyt3 = findViewById(R.id.pluslyt);
        Tlyt = findViewById(R.id.Tlyt);

        chart = findViewById(R.id.chart3);

        btn1.setOnClickListener(v -> {
            lyt1.setVisibility(View.VISIBLE);
            lyt2.setVisibility(View.GONE);
            Tlyt.setVisibility(View.GONE);
            yrange.setVisibility(View.VISIBLE);
           // getgraphs.setVisibility(View.GONE);

        });
        btn2.setOnClickListener(v -> {
            lyt2.setVisibility(View.VISIBLE);
            lyt1.setVisibility(View.GONE);
            yrange.setVisibility(View.GONE);
            Tlyt.setVisibility(View.VISIBLE);
           // getgraphs.setVisibility(View.VISIBLE);

        });
        btn3.setOnClickListener(v -> {

             if (!pause){

                 lyt3.setVisibility(View.VISIBLE);
                 Hrange3.setVisibility(View.VISIBLE);
                 btn3.setBackgroundResource(R.drawable.deleted);
                 pause = true;
             }else{
                 lyt3.setVisibility(View.GONE);
                 Hrange3.setVisibility(View.GONE);
                 btn3.setBackgroundResource(R.drawable.plusd);
                 pause = false;
             }
        });

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2015; i <= thisYear; i++) {

            if(i==2015){
                years.add("Select year");
            }
            else {
                years.add(Integer.toString(i));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinYear = findViewById(R.id.yearspin);
        Spinner spinYear1 = findViewById(R.id.yearspin1);
        Spinner onlyspinYear = findViewById(R.id.onlyyear);
        Spinner sp3 = findViewById(R.id.yearspin3);
        spinYear.setAdapter(adapter);
        spinYear1.setAdapter(adapter);
        onlyspinYear.setAdapter(adapter);
        sp3.setAdapter(adapter);

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                value = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        spinYear1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                value2 = parent.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        onlyspinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueyear = parent.getItemAtPosition(position).toString();
                getonlyyr();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value3 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> Month1 = new ArrayList<String>();
        for (int i = 0; i < Months.length; i++) {
           if(i==0){
               Month1.add("Select Month");
           }
           else{
             Month1.add(Months[i]);
           }
        }
        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Month1);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinMonths = findViewById(R.id.spinnerMonths);
        Spinner spinMonths1 = findViewById(R.id.spinnerMonths1);
        Spinner spinMonths3 = findViewById(R.id.spinnerMonths3);
        spinMonths.setAdapter(adapterMonths);
        spinMonths1.setAdapter(adapterMonths);
        spinMonths3.setAdapter(adapterMonths);

        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
              posss = String.valueOf(parent.getSelectedItemPosition());
              getspinnerdata();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        spinMonths1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                posss2 = String.valueOf(parent.getSelectedItemPosition());
                getspinnerdata2();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        spinMonths3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posss3 = String.valueOf(parent.getSelectedItemPosition());
                getspinnerdata3();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getspinnerdata(){

        if(posss.equals("1")||posss.equals("2")||posss.equals("3")||posss.equals("4")||posss.equals("5")||posss.equals("6")||posss.equals("7")||posss.equals("8")||posss.equals("9")){
            inc= "0"+posss;
        }
        else {
            inc= posss;
        }
        store = value+"-"+inc;
        Log.i("storess",store);

       StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/hreport.php", response -> {

           Log.i("sp1response",response);
            try {

                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    name = jsonobject.getString("hcount");
                    Hrange.setText(name);
                    Hrange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            Intent i = new Intent(ComparisonActivity.this,PriceHNames.class);
                            b.putString("sp1value",store);

                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    yy = value;
                   // getgraphs();
                    Log.i("name", String.valueOf(name));
                }
            } catch (JSONException e) {
                e.printStackTrace();

             //   Toast.makeText(ComparisonActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(ComparisonActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",store);
                params.put("category","hotelcount");
                Log.i("store", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ComparisonActivity.this));
        requestQueue.add(stringRequest);

    }
    private void getspinnerdata2(){

        if(posss2.equals("1")||posss2.equals("2")||posss2.equals("3")||posss2.equals("4")||posss2.equals("5")||posss2.equals("6")||posss2.equals("7")||posss2.equals("8")||posss2.equals("9")){
            inc2= "0"+posss2;
        }
        else {
            inc2= posss2;
        }

        store2 = value2+"-"+inc2;

        Log.i("storess2",store2);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/hreport.php", response -> {
            Log.i("sp2response",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    name1 = jsonobject.getString("hcount");
                    Hrange2.setText(name1);
                    Hrange2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            Intent i = new Intent(ComparisonActivity.this,PriceHNames.class);
                            b.putString("sp1value",store2);

                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    yy1=value2;
                   // getgraphs();

                    Log.i("name2", String.valueOf(name1));
                }
            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(ComparisonActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(ComparisonActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",store2);
                params.put("category","hotelcount");
                Log.i("store2", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ComparisonActivity.this));
        requestQueue.add(stringRequest);

    }
    private void getspinnerdata3(){


        if(posss3.equals("1")||posss3.equals("2")||posss3.equals("3")||posss3.equals("4")||posss3.equals("5")||posss3.equals("6")||posss3.equals("7")||posss3.equals("8")||posss3.equals("9")){

            inc3= "0"+posss3;
        }
        else {

            inc3= posss3;
        }

        store3 = value3+"-"+inc3;

        Log.i("storess2",store3);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/hreport.php", response -> {
            Log.i("sp3response",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    name3 = jsonobject.getString("hcount");
                    Hrange3.setText(name3);
                    Hrange3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            Intent i = new Intent(ComparisonActivity.this,PriceHNames.class);
                            b.putString("sp1value",store3);

                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    yy2 = value3;
                   // getgraphs();

                    Log.i("name3", String.valueOf(name3));

                }
            } catch (JSONException e) {
                e.printStackTrace();

              //  Toast.makeText(ComparisonActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(ComparisonActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("book_date",store3);
                params.put("category","hotelcount");
                Log.i("store2", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ComparisonActivity.this));
        requestQueue.add(stringRequest);


    }
    private void getonlyyr(){

        store1 = valueyear;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/hreport.php", response -> {

            Log.i("only_yr_res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("hcount");
                    yrange.setText(name);
                    yrange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            Intent i = new Intent(ComparisonActivity.this,PriceHNames.class);
                            b.putString("sp1value",store1);

                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    Log.i("namey", String.valueOf(name));

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ComparisonActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }, error -> {

            Toast.makeText(ComparisonActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",store1);
                params.put("category","hotelcount");
                Log.i("store", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(ComparisonActivity.this));
        requestQueue.add(stringRequest);

    }

   /* private void getgraphs(){

         hh=yy;
         hh1=yy1;
         hh2=yy2;
         n2=name3;
         nn =name;
         n1=name1;

    }

    public void graph(View view) {

        Intent intent = new Intent(ComparisonActivity.this,bargraph.class);
        Bundle bb = new Bundle();
        bb.putString("v1",hh);
        bb.putString("v2",hh1);
        bb.putString("c1",nn);
        bb.putString("c2",n1);
        if(hh2.equals("Select year")){
            bb.putString("v3", String.valueOf(0));
        }else{
            bb.putString("v3",hh2);
        }

        if(n2.equals("null")){
            bb.putString("c3", String.valueOf(0));
        } else{
            bb.putString("c3",n2);
        }

        Log.i("get",String.valueOf(bb));
        intent.putExtras(bb);
        startActivity(intent);
    }*/

}
