package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.CustomLoading;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancellationAct extends AppCompatActivity {

    Button htl,darshan,tour;
    long mindate,fdate,tdate;int dateflag=0;
    String FromDate,ToDate,StartDt,EndDt,cat;
    TextView FromTv,ToTv;
    LinearLayout From,To;
    RecyclerView recyclerView;
    CustomLoading cloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);

        fdate = System.currentTimeMillis();tdate = System.currentTimeMillis()+86400000L;
        FromDate = Utils.DatetoStr(fdate,0);ToDate = Utils.DatetoStr(tdate,0);


        cloading = new CustomLoading(this);
        cloading.setCancelable(false);
        assert cloading.getWindow()!=null;
        cloading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        InitViews();
        InitListeners();

    }

    private void InitViews() {
        htl = findViewById(R.id.hotelCb);
        darshan = findViewById(R.id.darshanCb);
        tour = findViewById(R.id.tourCb);
        From = findViewById(R.id.FDateLyt);
        To = findViewById(R.id.TDateLyt);
        FromTv = findViewById(R.id.fromTV);
        ToTv = findViewById(R.id.toTV);
        recyclerView = findViewById(R.id.cancleRcv);
    }

    private void InitListeners() {

        From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mindate = System.currentTimeMillis() - 7776000000L;
                dateflag = 1;
                datedialog();
            }
        });
        To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mindate = fdate;
                dateflag = 2;
                datedialog();
            }
        });
        htl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat="hotel";


                if(StartDt == null || EndDt == null ){
                    Toast.makeText(CancellationAct.this, "Select Date Range", Toast.LENGTH_SHORT).show();
                }else{ getdata();}

                htl.setBackgroundResource(R.drawable.select);
                htl.setTextColor(Color.parseColor("#000000"));
                tour.setTextColor(Color.parseColor("#ffffff"));
                darshan.setTextColor(Color.parseColor("#ffffff"));
                tour.setBackgroundResource(R.drawable.box_grey1);
                darshan.setBackgroundResource(R.drawable.box_grey1);

            }
        });
        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat="tour";


                if(StartDt == null || EndDt == null ){
                    Toast.makeText(CancellationAct.this, "Select Date Range", Toast.LENGTH_SHORT).show();
                }else{ gettourdata();}

                tour.setBackgroundResource(R.drawable.select);
                tour.setTextColor(Color.parseColor("#000000"));
                htl.setTextColor(Color.parseColor("#ffffff"));
                darshan.setTextColor(Color.parseColor("#ffffff"));
                htl.setBackgroundResource(R.drawable.box_grey1);
                darshan.setBackgroundResource(R.drawable.box_grey1);
            }
        });
        darshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat="darshan";

                if(StartDt == null || EndDt == null ){
                    Toast.makeText(CancellationAct.this, "Select Date Range", Toast.LENGTH_SHORT).show();
                }else{  getdarshandata();}

                darshan.setBackgroundResource(R.drawable.select);
                darshan.setTextColor(Color.parseColor("#000000"));
                tour.setTextColor(Color.parseColor("#ffffff"));
                htl.setTextColor(Color.parseColor("#ffffff"));
                tour.setBackgroundResource(R.drawable.box_grey1);
                htl.setBackgroundResource(R.drawable.box_grey1);
            }
        });
    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar calndr = Calendar.getInstance();
            calndr.set(year,month,day);

            if (dateflag == 1) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ncal.add(Calendar.DAY_OF_MONTH, 1);

                FromDate = Utils.DatetoStr(calndr.getTime(),0);ToDate = Utils.DatetoStr(ncal.getTime(),0);
                FromTv.setText(Utils.DatetoStr(calndr.getTime(),0));//ToTv.setText(Utils.DatetoStr(ncal.getTime(),1));

                StartDt = FromTv.getText().toString();

                mindate = calndr.getTimeInMillis();
                dateflag = 2;
                datedialog();

            } else if (dateflag == 2) {
                ToDate = Utils.DatetoStr(calndr.getTime(),0);
                ToTv.setText(Utils.DatetoStr(calndr.getTime(),0));

                EndDt = ToTv.getText().toString();

                //getdata();
            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();
        pickerDialog.getDatePicker().setMinDate(mindate);
        pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 + 7776000000L);
    }

    private void getdata() {

        cloading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/bhanu/cpanel_admin/booking/hotel_booking_reports/cancelled/6865446727eae9cbd513", response -> {

            recyclerView.setVisibility(View.VISIBLE);
         cloading.dismiss();
            Log.i("hres",response);

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("hotel_name"));
                    dataAdapter.setCarName(jobj.getString("contact_fname")+" "+jobj.getString("contact_lname"));
                    dataAdapter.setRName(jobj.getString("booking_room_type"));
                    dataAdapter.setTid(jobj.getString("pnr_no"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setInDate(jobj.getString("check_in_date"));
                    dataAdapter.setOutDate(jobj.getString("check_out_date"));

                    list.add(dataAdapter);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "No Data For This Dates", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("from_date",StartDt);
                params.put("to_date",EndDt);
                //params.put("category",Category);

                Log.i("hparams", String.valueOf(params));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void gettourdata() {

        cloading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/bhanu/cpanel_admin/booking/tour_booking_reports/cancelled/0/6865446727eae9cbd513", response -> {
        cloading.dismiss();
            recyclerView.setVisibility(View.VISIBLE);

            Log.i("tres",response);

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("search_city"));
                    dataAdapter.setCarName(jobj.getString("firstname")+" "+jobj.getString("lastname"));
                    dataAdapter.setRName(jobj.getString("sightseen_name"));
                    dataAdapter.setTid(jobj.getString("pnr_no"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setInDate(jobj.getString("travel_date"));
                    //dataAdapter.setOutDate(jobj.getString("check_out_date"));

                    list.add(dataAdapter);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "No Data For This Dates", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("from_date",StartDt);
                params.put("to_date",EndDt);
                //params.put("category",Category);

                Log.i("tparams", String.valueOf(params));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void getdarshandata() {
       cloading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/bhanu/cpanel_admin/booking/darshan_booking_reports/cancelled/0/6865446727eae9cbd513", response -> {
        cloading.dismiss();
            recyclerView.setVisibility(View.VISIBLE);

            Log.i("dres",response);

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("search_city"));
                    dataAdapter.setCarName(jobj.getString("firstname")+" "+jobj.getString("lastname"));
                    dataAdapter.setRName(jobj.getString("sightseen_name"));
                    dataAdapter.setTid(jobj.getString("pnr_no"));
                    dataAdapter.setGname(jobj.getString("supported_by"));
                    dataAdapter.setInDate(jobj.getString("travel_date"));
                    //dataAdapter.setOutDate(jobj.getString("check_out_date"));

                    list.add(dataAdapter);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "No Data For This Dates", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("from_date",StartDt);
                params.put("to_date",EndDt);
                //params.put("category",Category);

                Log.i("dparams", String.valueOf(params));

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
            return new hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cancellation, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);


            if(cat.equals("tour") || cat.equals("darshan")){
               vh.hnamekey.setText("City Name");
               vh.rnamekey.setText("Sightseen Name");
               vh.cinkey.setText("Travel Date");
               vh.coutllyt.setVisibility(View.GONE);
            }

                vh.hnameTv.setText(DDataAdapter.getHname());
                vh.cnameTv.setText(DDataAdapter.getCarName());
                vh.TripIdTV.setText(DDataAdapter.getTid());
                vh.RtnameTv.setText(DDataAdapter.getRName());
                vh.supbyTv.setText(DDataAdapter.getGname());
                vh.cin.setText(DDataAdapter.getInDate());
                vh.cout.setText(DDataAdapter.getOutDate());
        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,cnameTv,TripIdTV,RtnameTv,supbyTv,cin,cout,hnamekey,cnamekey,rnamekey,supkey,cinkey;
            LinearLayout coutllyt;
            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.HName);
                cnameTv = iv.findViewById(R.id.CName);
                TripIdTV = iv.findViewById(R.id.Pnrno);
                RtnameTv = iv.findViewById(R.id.Rtname);
                supbyTv = iv.findViewById(R.id.supby);
                cin = iv.findViewById(R.id.cinTv);
                cout = iv.findViewById(R.id.coutTv);
                hnamekey = iv.findViewById(R.id.HNamekey);
              //  cnamekey = iv.findViewById(R.id.cnamekey); 918010064004120
                rnamekey = iv.findViewById(R.id.rnamekey);
               // supkey = iv.findViewById(R.id.sbykey);
                cinkey = iv.findViewById(R.id.ckey);
                coutllyt = iv.findViewById(R.id.coutllyt);

            }
        }
    }

}
