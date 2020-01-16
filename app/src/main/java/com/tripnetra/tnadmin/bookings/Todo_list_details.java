package com.tripnetra.tnadmin.bookings;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Todo_list_details extends AppCompatActivity {


    String Mid,contact, status,reqdate,Fid,plc,desc,resp,upData,Url,upstatus,flwup,detailsData,result;
    TextView mrgdtv,desctv,mrgplctv,contacttv, prvsresptv,newresptv,svbtn,texts;
    EditText addet;
    WebView resWV;

    String[] statusspn = { "Status", "WILLING", "CLOSED" , "RECHECK", "CONFIRMED"};
    Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_details);


        Mid = Objects.requireNonNull(getIntent().getExtras()).getString("id");//Recycler_todo_id
        Fid = getIntent().getExtras().getString("pnr");//fcm_todo_id
        mrgplctv=findViewById(R.id.mrgplc1TV);
        contacttv=findViewById(R.id.mrgcntTV);
        mrgdtv=findViewById(R.id.mrgdt1TV);
        desctv=findViewById(R.id.mrgdescTV);
        prvsresptv =findViewById(R.id.prvsTV);
        resWV =findViewById(R.id.prvsWV);
        newresptv=findViewById(R.id.newTV);
        addet=findViewById(R.id.addET);
        svbtn=findViewById(R.id.savBtn);
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

        if(Mid!=null){
            Url="https://tripnetra.com/cpanel_admin/followup/edit_request_details/"+ Mid +"/6865446727eae9cbd513";
        }
        else{
            Url="https://tripnetra.com/cpanel_admin/followup/edit_request_details/"+ Fid +"/6865446727eae9cbd513";
        }
        Follow_up_details();

    }


    public void Follow_up_details(){


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
                    resp = jsonobject.getString("response");//desc
                    flwup = jsonobject.getString("followup_date");//desc
                    result= Html.fromHtml(resp).toString();
                    resWV.loadData(resp,"text/html","UTF-8");

                    mrgdtv.setText(reqdate);desctv.setText(desc);mrgplctv.setText(plc);contacttv.setText(contact);
                    Toast.makeText(this, contact+" "+status+" "+reqdate, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

            Toast.makeText(Todo_list_details.this, "Something went wrong Try again.", Toast.LENGTH_LONG).show();

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                if(Mid!=null){
                    params.put("request_details_id",Mid);
                }
                else{
                    params.put("request_details_id",Fid);
                }

                params.put("type","folw");
                Log.i("india", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue((this));
        requestQueue.add(stringRequest);

    }

    public void prvsconversation(View view) {

        resWV.setVisibility(View.VISIBLE);
    }



    public void newconversation(View view) {
        addet.setVisibility(View.VISIBLE);
       // svbtn.setVisibility(View.VISIBLE);
    }


    public void Updatedatas(View view) {


      //  Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

        upData=addet.getText().toString().trim();

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
                params.put("response",(upData==null) ? resp : upData);//response
                params.put("contact",contact);//contact
                params.put("date",reqdate);//date
                params.put("followup_date",flwup);//followupdate
                params.put("status",(upstatus.equals("Status")) ? status : upstatus);//status
                params.put("admin_name","Mr.-kamalakar-kandala");//username
                Log.i("ps4", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent(this, DashBoardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        super.onBackPressed();
    }

    public void shrdata(View view) {

        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        detailsData="Phone no :"+contact+"\n"+"Place :"+plc+"\n"+"Marriage Date :"+reqdate+"\n"+"Description :"+desc+"\n"
                    +"Previous Conversation :"+"\n"+"\n"+result;
        ClipData clip = ClipData.newPlainText("Copied", detailsData);
        clipboard.setPrimaryClip(clip);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,detailsData );
        this.startActivity(Intent.createChooser(share, "Share Marriage Details to"));
    }
}
