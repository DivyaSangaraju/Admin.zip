package com.tripnetra.tnadmin.bookings;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.DashBoardActivity;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.utils.G46567;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Marriage_Add_Rsp extends AppCompatActivity {

    Button Save,CinBtn,ShareBtn;
    EditText addres;
    String Mid,upData,contact, status,reqdate,Followupdt,UserName,
            Fid,Url,plc,desc,user,detailsData,resp,upstatus,prvsflwup,result;
    TextView getdate,mrgplctv,contacttv,desctv,mrgdtv,updtv;
    DatePickerDialog datePickerDialog;
    int calmonth,calday;
    WebView respWv;
    String[] statusspn = { "Status", "WILLING", "CLOSED" , "RECHECK", "CONFIRMED"};
    Spinner spin;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marriage_add_rsp);
        assert getIntent().getExtras() != null;
        UserName=((G46567) this.getApplicationContext()).getUserName();//username

        Toast.makeText(this, UserName, Toast.LENGTH_SHORT).show();
        Mid = getIntent().getExtras().getString("id");//Recycler_mrg_id
        Fid = getIntent().getExtras().getString("pnr");//fcm_mrg_id

        Toast.makeText(this,  user, Toast.LENGTH_SHORT).show();
        if(Mid!=null){
            Url="https://tripnetra.com/cpanel_admin/followup/edit_request_details/"+ Mid +"/6865446727eae9cbd513";
        }
        else{
            Url="https://tripnetra.com/cpanel_admin/followup/edit_request_details/"+ Fid +"/6865446727eae9cbd513";
        }

        Save=findViewById(R.id.saveBtn);
        addres=findViewById(R.id.addresET);
        CinBtn = findViewById(R.id.cinBtn);
        getdate=findViewById(R.id.fromTV);
        mrgplctv=findViewById(R.id.mrgplcTV);
        contacttv=findViewById(R.id.cntctTV);
        desctv=findViewById(R.id.descTV);
        mrgdtv=findViewById(R.id.mrgdtTV);
        respWv =findViewById(R.id.respWV);
        updtv =findViewById(R.id.updtTV);
        ShareBtn =findViewById(R.id.shareBtn);
        mrg_details();

        getdate.setOnClickListener(view -> {
            dates();
        });

        updtv.setOnClickListener(view -> {
            addres.setVisibility(View.VISIBLE);
        });


        spin=findViewById(R.id.spinner);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,statusspn);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("pofday",statusspn[position]);
                upstatus = statusspn[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void dates(){


        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {


                calmonth = (monthOfYear +1);
                Log.i("calmonth", String.valueOf(calmonth));
                String sMonth;
                if (calmonth < 10) {
                    sMonth = "0"+ calmonth;
                } else {
                    sMonth = String.valueOf(calmonth);
                }


                calday = dayOfMonth;
                Log.i("calday", String.valueOf(calday));
                String sday;
                if (calday < 10) {
                    sday = "0"+ calday;
                } else {
                    sday = String.valueOf(calday);
                }

                 Followupdt=  sday + "-" + sMonth + "-" + year;
                    Log.i("Followupdt", Followupdt);

                    getdate.setText(Followupdt);

            }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public void mrg_details(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/adminpanel/marriage_requests1.php", response -> {

            try {

                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    contact = jsonobject.getString("request_contact_number");//contact
                    status = jsonobject.getString("request_status");//status
                    reqdate = jsonobject.getString("request_date");//req_dt
                    plc = jsonobject.getString("request_place");//place
                    desc = jsonobject.getString("request_description");//desc
                    resp=jsonobject.getString("response");
                    prvsflwup=jsonobject.getString("followup_date");
                    respWv.loadData(resp,"text/html", "UTF-8");
                    result = Html.fromHtml(resp).toString();
                    mrgdtv.setText(reqdate);desctv.setText(desc);mrgplctv.setText(plc);contacttv.setText(contact);
                 //   Toast.makeText(this, contact+" "+status+" "+reqdate, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

            Toast.makeText(Marriage_Add_Rsp.this, "Something went wrong Try again.", Toast.LENGTH_LONG).show();

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                if(Mid!=null){
                    params.put("id",Mid);
                }
                else{
                    params.put("id",Fid);
                }

                params.put("type","get");
                Log.i("india", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue((Marriage_Add_Rsp.this));
        requestQueue.add(stringRequest);

    }


    public void Updatedata(View view) {

        upData=addres.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Url, response -> {

            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            finish();

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                if(Mid!=null){
                    params.put("request_details_id", Mid);
                }
                else{
                    params.put("request_details_id", Fid);
                }
                params.put("response",upData);//response
                params.put("contact",contact);//contact
                params.put("date",reqdate);//date
                params.put("followup_date",(Followupdt==null) ? prvsflwup : Followupdt);//followupdate
                params.put("status",(upstatus.equals("Status")) ? status : upstatus);//status
                params.put("admin_name","Mr.-kamalakar-kandala");//username
                Log.i("p9", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        
    }


    @Override
    public void onBackPressed() {
        startActivity( new Intent(this, DashBoardActivity.class).
                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        super.onBackPressed();
    }

    public void sharedata(View view) {

        ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        detailsData="Phone no :"+contact+""+"\n"+"Marriage Date :"+""+reqdate+"\n"+"Place :"+""+plc+"\n"+"Description :"+desc+"\n"+"\n"+"Previous Conversation :"+"\n"+result;
        ClipData clip = ClipData.newPlainText("Copied", detailsData);
        clipboard.setPrimaryClip(clip);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,detailsData );
        view.getContext().startActivity(Intent.createChooser(share, "Share Marriage Details to"));

    }
}
