package com.tripnetra.tnadmin.Analytics_price;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profits_activtiy extends AppCompatActivity {
    Button CinBtn,CoutBtn;
    String FromDate,ToDate,category;
    long mindate;int dateflag=0;
    TextView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profits_activtiy);

        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);
        pro=findViewById(R.id.pTV);
        long fdate = System.currentTimeMillis(),tdate = System.currentTimeMillis();
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

    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Profits_activtiy.this, (view, year, month, day) -> {
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

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ToDate = Utils.DatetoStr(calndr.getTime(),0);
                ncal.add(Calendar.DATE,1);
                CoutBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                category="hotel";
                gethotelprice();

            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }

    private void gethotelprice() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/cpanel_admin/booking/get_profit/6865446727eae9cbd513", response -> {

            Toast.makeText(this, response ,Toast.LENGTH_SHORT).show();
            pro.setText(response);
            pro.setVisibility(View.VISIBLE);
            pro.setAnimation( AnimationUtils.loadAnimation(Profits_activtiy.this,R.anim.flash_leave_now));

        }, error -> {
         //   Toast.makeText(Profits_activtiy.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            Toast.makeText(Profits_activtiy.this, error.toString(), Toast.LENGTH_LONG).show();
        }
        )


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("type",category);
                params.put("from_date", FromDate);
                params.put("to_date", ToDate);
                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000000,DefaultRetryPolicy.
                DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Profits_activtiy.this));
        stringRequest.setShouldCache(true);


        requestQueue.add(stringRequest);
    }
}
