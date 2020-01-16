package com.tripnetra.tnadmin.bookings;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.CustomLoading;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.rest.VolleyRequester;
import com.tripnetra.tnadmin.utils.Utils;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class edittourvoucher extends AppCompatActivity implements View.OnClickListener {

    Bundle receive;
    String getPnrno,getslctsts,sfname,slname,mail,
    semail,stdate,sphn,flag,check,cin,
    URL="https://tripnetra.com/prasanth/androidphpfiles/adminpanel/updatetourvoucher.php";
    NiceSpinner status;
    List<String> getStatus,replacestatus;
    EditText fname,lname,email,trvl,phn;
    Button save;
    CheckBox send;
    CustomLoading cloading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittourvoucher);
        status=findViewById(R.id.statusspn);
        fname=findViewById(R.id.fnameEt);
        lname=findViewById(R.id.lnameEt);
        email=findViewById(R.id.emailEt);
        trvl=findViewById(R.id.travelEt);
        phn=findViewById(R.id.phnEt);
        save=findViewById(R.id.savedata);
        send=findViewById(R.id.cstresend);
        send.setOnClickListener(this);
        receive=getIntent().getExtras();
        assert receive != null;
        getPnrno=receive.getString("PnrNo");
        edit_reports();

        if(!send.isChecked()){

            flag="Dontsend";
        }
        cloading = new CustomLoading(this);
        cloading.setCancelable(false);
        Objects.requireNonNull(cloading.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cloading.show();

    }


    private void edit_reports() {

        getStatus=new ArrayList<>();
        replacestatus=new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/prasanth/androidphpfiles/adminpanel/twoquery.php", response -> {
            cloading.dismiss();
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    fname.setText(jobj.getString("firstname"));
                    lname.setText(jobj.getString("lastname"));
                    email.setText(jobj.getString("email_id"));
                    trvl.setText(jobj.getString("travel_date"));
                    phn.setText(jobj.getString("phone"));
                     check=jobj.getString("booking_status");
                    cin=jobj.getString("checkin_date");
                    Log.i("ch",check);
                    getStatus.add(jobj.getString("booking_status"));
                    replacestatus= Collections.singletonList(getStatus.toString().replace("[", "").replace("]", ""));
                    status.attachDataSource(getStatus);

                    String[] st = new String[]{check, "CANCELLED", "PROCESS"};
                    List<String> stlist = Arrays.asList(st);
                    String[] st1 = new String[]{check, "CANCELLED", "CONFIRMED"};
                    List<String> stlist1 = Arrays.asList(st1);
                    String[] st2 = new String[]{check, "PROCESS", "CONFIRMED"};
                    List<String> stlist2 = Arrays.asList(st2);


                     if(check.contains("CONFIRMED")){
                        status.attachDataSource(stlist);}


                     if(check.contains("PROCESS")){
                        status.attachDataSource(stlist1);}


                    if(check.contains("CANCELLED")){
                        status.attachDataSource(stlist2);}


                    status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                    if(check.contains("CONFIRMED")){
                       getslctsts=stlist.get(i);}

                    if(check.contains("PROCESS")){
                        getslctsts=stlist1.get(i);}

                    if(check.contains("CANCELLED")){
                        getslctsts=stlist2.get(i);
                    }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();

                  Toast.makeText(edittourvoucher.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

        }, error -> {


        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("Pnrno",getPnrno);
                Log.i("params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(edittourvoucher.this);
        requestQueue.add(stringRequest);
    }




    @Override
    public void onClick(View view) {
        final Dialog log=new Dialog(this);
        if (view.getId() == R.id.cstresend) {
            if (send.isChecked())
                log.setContentView(R.layout.vou_resend);
            Objects.requireNonNull(log.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Button Y = log.findViewById(R.id.yes);
            Button N = log.findViewById(R.id.no);
            Y.setOnClickListener(view1 -> {
                flag = "Resend";
                mail = "cmail";
                log.dismiss();

            });

            N.setOnClickListener(view12 -> {
                log.dismiss();
                flag = "no";

            });
            log.show();
        }
    }


    private void update(){


        sfname=fname.getText().toString().trim();
        slname=lname.getText().toString().trim();
        semail=email.getText().toString().trim();
        stdate=trvl.getText().toString().trim();
        sphn=phn.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, response -> {


            Toast.makeText(this, "Sucessfully inserted", Toast.LENGTH_SHORT).show();
        }, error -> {

            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("fname", sfname); //ok
                params.put("lname", slname); //ok
                params.put("email_id", semail);//ok
                params.put("darshan_date", stdate);//ok
                params.put("contact", sphn);//ok
                params.put("ex_aminity", "");//ok
                params.put("customer_mail", (mail==null) ? " ":mail);//ok
                params.put("checkin_date", (cin==null) ? " ":cin);//ok
                params.put("booking_status", (getslctsts==null) ? check : getslctsts);//ok
                params.put("pnr", getPnrno);//todo


                Log.i("ram", String.valueOf(params));



                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(this));
        requestQueue.add(stringRequest);

    }


    public void sav(View view) {
        switch (flag) {
            case "Resend" : update();
                break;
            case "Dontsend": case "no": update();
                break;
        }
}}

