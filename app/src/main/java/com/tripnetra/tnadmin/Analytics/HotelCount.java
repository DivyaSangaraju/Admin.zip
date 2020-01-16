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

public class HotelCount extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button CinBtn,CoutBtn,checkin,booking;
    TextView HCount;
    String position;
    LinearLayout LLTHCount;
    String FromDate,ToDate;
    long mindate;int dateflag=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_count);

        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);
        checkin=findViewById(R.id.checkin);
        booking=findViewById(R.id.booking);
        LLTHCount = findViewById(R.id.HotelcountLLt);
        HCount = findViewById(R.id.hotelcountTV);

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

       checkin.setOnClickListener(view -> {
           gethotelcount();
           position=String.valueOf(0);
           checkin.setBackgroundResource(R.drawable.select);
           booking.setBackgroundResource(R.drawable.boxfill_radius);
       });

       booking.setOnClickListener(view -> {
           gethotelcount();
           position=String.valueOf(1);
           booking.setBackgroundResource(R.drawable.select);
           checkin.setBackgroundResource(R.drawable.boxfill_radius);
       });

        LLTHCount.setOnClickListener(v -> {
            Intent intent = new Intent(HotelCount.this,CityNames.class);
            Bundle bundle = new Bundle();
            bundle.putString("position",position);
            bundle.putString("FromDate",FromDate);
            bundle.putString("ToDate",ToDate);
            intent.putExtras(bundle);
            startActivity(intent);

            Log.i("intent", String.valueOf(bundle));
        });


    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(HotelCount.this, (view, year, month, day) -> {
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

    private void gethotelcount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/gettourscount.php", response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String name = jsonobject.getString("bcount");

                    if(!name.equals("")){
                    HCount.setText(name);
                    HCount.setVisibility(View.VISIBLE);
                    } else{
                        HCount.setText("0");
                        HCount.setVisibility(View.VISIBLE);
                    }
                    Log.i("name", String.valueOf(name));
                }
            } catch (JSONException e) {
                  e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(HotelCount.this, "Something went wrong.", Toast.LENGTH_LONG).show();
        })


       {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("type",position);
                params.put("category","hotel");

                if(position.equals("0")){
                    params.put("check_in_date", FromDate);
                    params.put("check_out_date", ToDate);

                } else{
                    params.put("book_date",FromDate);
                    params.put("book_end_date",ToDate);
                }
                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(HotelCount.this));
        requestQueue.add(stringRequest);
    }
}
