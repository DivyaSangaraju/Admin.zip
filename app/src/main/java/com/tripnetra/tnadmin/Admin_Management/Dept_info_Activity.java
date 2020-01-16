package com.tripnetra.tnadmin.Admin_Management;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dept_info_Activity extends AppCompatActivity {
    NiceSpinner dept,desg;
    LinearLayout Mainlyt;
    String spinnerstus1,spinnerstus2,FName,LName,Email,Password,MobileNo,Status,
    URL="https://tripnetra.com/cpanel_admin/admin/add_admin/6865446727eae9cbd513";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_info);


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        FName = bundle.getString("firstname");
        LName = bundle.getString("lastname");
        Email = bundle.getString("email");
        Password = bundle.getString("password");
        MobileNo = bundle.getString("mobileno");
        Status = bundle.getString("status");
        dept=findViewById(R.id.deptNS);
        desg=findViewById(R.id.desigNS);
        Mainlyt = findViewById(R.id.Mainlayout);
        //int dt= Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);;

        List<String> departments = new ArrayList<>();
        departments.add("Select Department");
        departments.add("Administrator");
        departments.add("Support");
        String[] desgs1 = new String[]{"Select Designation", "Super Admin", "Admin"};
        List<String> desglist = Arrays.asList(desgs1);

        String[] desgs2 = {"Select Designation","Support Executive","Team Lead"};
        List<String> desglist2 = Arrays.asList(desgs2);

        dept.attachDataSource(departments);
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {


                spinnerstus1 = departments.get(position);
                if(spinnerstus1.equals("Administrator")){
                    spinnerstus1 = "2";
                    desg.attachDataSource(desglist);
                }
                if(spinnerstus1.equals("Support")){
                    spinnerstus1 = "3";
                    desg.attachDataSource(desglist2);
                }
                Log.i("spinnerstus1",spinnerstus1);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });


        desg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {

                if (spinnerstus1.equals("2")) {

                    spinnerstus2 = desglist.get(position);

                    if (spinnerstus2.equals("Super Admin")) {
                        spinnerstus2 = "1";
                    }
                    if (spinnerstus2.equals("Admin")) {
                        spinnerstus2 = "2";
                    }

                }else {
                    spinnerstus2 = desglist2.get(position);

                    if (spinnerstus2.equals("Support Executive")) {
                        spinnerstus2 = "3";
                    }
                    if (spinnerstus2.equals("Team Lead")) {
                        spinnerstus2 = "4";
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

    }


    public void addadmin(View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, response -> {


            Snackbar.make(Mainlyt,"Successfully added new admin",Snackbar.LENGTH_SHORT).show();
             }, error -> {

            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("email_id", Email);
                params.put("first_name", FName);
                params.put("last_name", LName);
                params.put("mobile_no", MobileNo);
                params.put("new_password", Password);
                params.put("new_password_text", Password);
                params.put("confirm_password", Password);
                params.put("salution", "Mr.");
                params.put("admin_status", Status);
                params.put("role_id",spinnerstus1 );
                params.put("designation_details_id", spinnerstus2);
                params.put("name", "");
                params.put("type", "");
                params.put("tmp_name", "");
                params.put("error", "4");
                params.put("size", "0");
                params.put("emailidflag", "true");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue((this));
        requestQueue.add(stringRequest);

    }

}
