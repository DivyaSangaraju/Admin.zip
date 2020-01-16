package com.tripnetra.tnadmin.Notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.rest.VolleyRequester;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Darshan_Assign_second extends AppCompatActivity {

    String tripid,passnm,packnm,trvldt,sid,getitenry,gettkn,Suppname,SuppId,fcmsid,SuppTkn;
    WebView itview;
    LinearLayout asgnly;
    AutoCompleteTextView HnameTv;
    List<String> spnamelist,idslist,tokenlist;
    Button btnsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darshan_assign_second);
        Bundle getdata = getIntent().getExtras();
        assert getdata!=null;
        tripid = getdata.getString("Tripid");
        passnm = getdata.getString("Passnm");
        packnm = getdata.getString("packnm");
        trvldt = getdata.getString("trvltv");
        sid = getdata.getString("sid");
        fcmsid = getdata.getString("fid");

        Toast.makeText(this, fcmsid, Toast.LENGTH_SHORT).show();
        itview=findViewById(R.id.itview);
        asgnly=findViewById(R.id.asgnlyt);
        HnameTv=findViewById(R.id.autocompTv);
        btnsend=findViewById(R.id.btnsend);

        func_Itenry();
        func_Assign();

    }

    public  void func_Itenry() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/notfydarshan.php", response -> {

            try {
                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = jarr.getJSONObject(i);

                    getitenry= json.getString("sightseen_itinerary");
                    itview.loadData(getitenry,"text/html", "UTF-8");
                }

                Log.i("il", String.valueOf(getitenry));
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> {
            Toast.makeText(getApplicationContext(),"something went wrong Try again",Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (sid != null) {
                    params.put("sid", sid);
                } else {
                    params.put("sid", fcmsid);

                }

                Log.i("para", String.valueOf(params));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Assignto(View view) {
        func_Assign();
        asgnly.setVisibility(View.VISIBLE); }

    private void func_Assign() {
        spnamelist = new ArrayList<>();
        idslist = new ArrayList<>();
        tokenlist = new ArrayList<>();
        Map<String, String> params = new HashMap<>();

        new VolleyRequester(this).ParamsRequest(1, "https://tripnetra.com/prasanth/androidphpfiles/test/adminpanel/supplier_names.php", null, params, false, response -> {
            try {

                JSONArray jarr = new JSONArray(response);

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = jarr.getJSONObject(i);
                    spnamelist.add(json.getString("supplier_name"));
                    idslist.add(json.getString("supplier_details_id"));
                    tokenlist.add(json.getString("fcm_token"));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.textview_layout, spnamelist);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                HnameTv.setThreshold(1);
                HnameTv.setAdapter(dataAdapter);

                HnameTv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                    Suppname = String.valueOf(arg0.getItemAtPosition(arg2));
                    int ii = spnamelist.indexOf(Suppname);
                    SuppId = idslist.get(ii);
                    SuppTkn = tokenlist.get(ii);
                //    func_gettoken();
                    Toast.makeText(this, SuppTkn+""+SuppId+""+Suppname, Toast.LENGTH_SHORT).show();
                    Log.i("s",SuppTkn);

                    Utils.SendNotification(this, "Package Assigned"+"-"+sid);

                    HnameTv.clearFocus();
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });

            } catch (JSONException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void sendntfy(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/prasanth/androidphpfiles/test/adminpanel/snd_pack_ntfy.php", response -> {
              Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

        }, error -> {
            Toast.makeText(getApplicationContext(),"something went wrong Try again",Toast.LENGTH_SHORT).show();
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fcm_token", SuppTkn);
             //  params.put("fcm_token", "d9QKBOgklrE:APA91bEehEanzLnIpxnD1ZyKNOrX5Gi0z-mOnVa-z-wZGVu7KdnF9yre1LPBAZeTom_R4hAUjG0CHrR8Fy_CqLO8lteTdd9RmIsH5mykkQOk8EIt7c663TiaUVDDFr84J22b8YCk6gdR");
                Log.i("ja", String.valueOf(params));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
