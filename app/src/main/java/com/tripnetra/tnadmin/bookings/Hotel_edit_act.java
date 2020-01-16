package com.tripnetra.tnadmin.bookings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Hotel_edit_act extends AppCompatActivity {
    String Pnrno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_edit_act);

        Pnrno= Objects.requireNonNull(getIntent().getExtras()).getString("pnr");

        Toast.makeText(this, Pnrno, Toast.LENGTH_SHORT).show();
    }

    public void Todo_List() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/bhanu/cpanel_admin/followup/followup_requests/"+"/6865446727eae9cbd513", response -> {



            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {



                    JSONObject jobj = jsonarray.getJSONObject(i);

                 /* String =  jobj.getString("added_by");//
                    String =    jobj.getString("added_date");//
                    String =   jobj.getString("request_description");//
                    String = jobj.getString("request_contact_number");
*/

                }
            }
            catch (JSONException e) {

              e.printStackTrace();

            }

        }, error -> {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("mrg_followup",Pnrno);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
