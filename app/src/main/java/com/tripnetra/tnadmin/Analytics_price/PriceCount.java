package com.tripnetra.tnadmin.Analytics_price;

import android.app.DatePickerDialog;
import android.graphics.Paint;
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

public class PriceCount extends AppCompatActivity {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button CinBtn,CoutBtn,bkngs;


    TextView HCount,TCount,DCount,Note;String position;LinearLayout LLTHCount;String FromDate,ToDate;long mindate;int dateflag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_count);

        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);

        LLTHCount = findViewById(R.id.HotelcountLLt);
        HCount = findViewById(R.id.hotelcountTV);
        TCount = findViewById(R.id.tourscountTV);
        DCount = findViewById(R.id.darshancountTV);
        bkngs = findViewById(R.id.bkng);
         Note =  findViewById(R.id.note);
         Note.setPaintFlags(Note.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        long fdate = System.currentTimeMillis(),tdate = System.currentTimeMillis();

        FromDate = Utils.DatetoStr(fdate,0);ToDate = Utils.DatetoStr(tdate,0);

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


       bkngs.setOnClickListener(view -> {
           position=String.valueOf(0);
           LLTHCount.setVisibility(View.VISIBLE);
           gethotelprice();
           gettoursprice();
           getdarshanprice();
       });

       /* HCount.setOnClickListener(v -> {
            Intent intent = new Intent(PriceCount.this,ComparisonActivity.class);
            startActivity(intent);
        });
        TCount.setOnClickListener(v -> {
            Intent intent = new Intent(PriceCount.this,Tour_Range.class);
            startActivity(intent);
        });
        DCount.setOnClickListener(v -> {
            Intent intent = new Intent(PriceCount.this,Darshan_Range.class);
            startActivity(intent);
        });*/

       HCount.setEnabled(false);TCount.setEnabled(false);DCount.setEnabled(false);
    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(PriceCount.this, (view, year, month, day) -> {
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
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }

    private void gethotelprice() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/extranet/prices.php", response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String name = jsonobject.getString("tp");
                    HCount.setText("Hotel Price----- "+name);
                  //  Log.i("name","name")

                }
            } catch (JSONException e) {
                e.printStackTrace();
              //  Toast.makeText(PriceCount.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            Toast.makeText(PriceCount.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                 params.put("category","hotelprice");
                if(position.equals("0")){
                    params.put("bsdate", FromDate);
                    params.put("bedate", ToDate);
                } else{
                    params.put("rmsdate",FromDate);
                    params.put("rmedate",ToDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(PriceCount.this));
        requestQueue.add(stringRequest);
    }

    private void gettoursprice() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/extranet/prices.php", response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name1 = jsonobject.getString("hp");
                    TCount.setText("Tours Price----- "+name1);
                   // Log.i("name1", String.valueOf(name1));

                }
            } catch (JSONException e) {
                e.printStackTrace();

              //  Toast.makeText(PriceCount.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            Toast.makeText(PriceCount.this, "Something went wrong.", Toast.LENGTH_LONG).show();

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("category","tourprice");
                if(position.equals("0")){
                    params.put("bsdate", FromDate);
                    params.put("bedate", ToDate);

                } else{
                    params.put("rmsdate",FromDate);
                    params.put("rmedate",ToDate);
                }
                Log.i("ram", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(PriceCount.this));
        requestQueue.add(stringRequest);
    }

    private void getdarshanprice() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/extranet/prices.php", response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name2 = jsonobject.getString("sp");
                    DCount.setText("Darshan Price----- "+name2);
                    Log.i("name2", String.valueOf(name2));

                }
            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(PriceCount.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {

            Toast.makeText(PriceCount.this, "Something went wrong.", Toast.LENGTH_LONG).show();

        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("category","darshanprice");

                if(position.equals("0")){
                    params.put("bsdate", FromDate);
                    params.put("bedate", ToDate);

                } else{
                    params.put("rmsdate",FromDate);
                    params.put("rmedate",ToDate);
                }
                Log.i("ram1", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(PriceCount.this));
        requestQueue.add(stringRequest);
    }
}
