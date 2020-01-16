package com.tripnetra.tnadmin.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Darshan_recycler_first extends AppCompatActivity {
    RecyclerView recyclerView;
     String today,fcmpnrno,fcmpnrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.darshan_recycler_act);
        recyclerView=findViewById(R.id.cycle);
        today= String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()));
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        getdata();
//        fcmpnrno = Objects.requireNonNull(getIntent().getExtras()).getString("pnr");
    //    fcmpnrid = getIntent().getExtras().getString("fid");
        Log.i("data",fcmpnrid+""+fcmpnrno);
    }


    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/jayakumar/tours/getBookingData", response -> {

            recyclerView.setVisibility(View.VISIBLE);

            Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("sightseen_name"));
                    dataAdapter.setGname(jobj.getString("firstname") + " " + jobj.getString("lastname"));
                    dataAdapter.setRName(jobj.getString("search_city"));
                    dataAdapter.setHid(jobj.getString("pnr_no"));
                    dataAdapter.setStatus(jobj.getString("booking_status"));
                    dataAdapter.setMobile(jobj.getString("phone"));
                    dataAdapter.setInDate(jobj.getString("checkin_date"));
                    dataAdapter.setOutDate(jobj.getString("travel_date"));
                    dataAdapter.setCap2(jobj.getString("sightseen_booking_id"));
                    dataAdapter.setCap1(jobj.getString("sightseen_id"));

                    dataAdapter.setNewName(jobj.getString("adults")+" "+"Adults"+" "+jobj.getString("childs")+" "+"Kids");

                    list.add(dataAdapter);

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Darshan_recycler_first.this));
                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();
             //   Toast.makeText(this, "No Bookings Done Yet", Toast.LENGTH_SHORT).show();
               recyclerView.setVisibility(View.GONE);

            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("today",today);
                Log.i("param", String.valueOf(params));


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }


    public class hotel_recycle_adapter  extends RecyclerView.Adapter<hotel_recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        hotel_recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public hotel_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rcv__ntf, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.Tripid.setText(DDataAdapter.getHname());
            vh.Passnm.setText(DDataAdapter.getGname());
            vh.packnm.setText(DDataAdapter.getHid());
            vh.trvltv.setText(DDataAdapter.getOutDate());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView Tripid,Passnm,packnm,trvltv;
            Bundle senddata=new Bundle();
            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                Tripid = iv.findViewById(R.id.tripTV);
                Passnm = iv.findViewById(R.id.PassTV);
                packnm = iv.findViewById(R.id.PackTV);
                trvltv = iv.findViewById(R.id.TrvlTv);
                iv.setOnClickListener(v -> {

                    Intent intent = new Intent(context, Darshan_Assign_second.class);
                    senddata.putString("Tripid", listadapter.get(getAdapterPosition()).getHname());
                    senddata.putString("Passnm", listadapter.get(getAdapterPosition()).getGname());
                    senddata.putString("packnm",listadapter.get(getAdapterPosition()).getHid());
                    senddata.putString("trvltv",listadapter.get(getAdapterPosition()).getOutDate());
                    senddata.putString("sid",listadapter.get(getAdapterPosition()).getCap1());

                    intent.putExtras(senddata);
                    context.startActivity(intent);
                });

            }
        }
    }

}
