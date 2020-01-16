package com.tripnetra.tnadmin.Analytics_price;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class cancelled_reports extends AppCompatActivity implements View.OnClickListener {

    Button CinBtn,CoutBtn;
    String FromDate,ToDate,category;
    long mindate;int dateflag=0;
    CheckBox htl,drshn,tour;
    RecyclerView recyclerView;
    String getname,getcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelled_activity);

        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);
        htl = findViewById(R.id.htlchck);
        htl.setOnClickListener(this);
        drshn = findViewById(R.id.drshnchck);
        drshn.setOnClickListener(this);
        tour = findViewById(R.id.tourchck);
        tour.setOnClickListener(this);
        recyclerView=findViewById(R.id.recyl);
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

        DatePickerDialog pickerDialog = new DatePickerDialog(cancelled_reports.this, (view, year, month, day) -> {
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

            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.htlchck:
                if (htl.isChecked())
                    drshn.setChecked(false);
                    tour.setChecked(false);
                    category="hotel";
                    getdata();
                  //  Toast.makeText(getApplicationContext(), "Hotel", Toast.LENGTH_LONG).show();
                break;


            case R.id.drshnchck:
                if (drshn.isChecked())
                    htl.setChecked(false);
                    tour.setChecked(false);
                    category="darshan";
                    getdata();
                 //   Toast.makeText(getApplicationContext(), "Darshan", Toast.LENGTH_LONG).show();
                break;

            case R.id.tourchck:
                if (tour.isChecked())
                    htl.setChecked(false);
                    drshn.setChecked(false);
                    category="tour";
                    getdata();
                  //  Toast.makeText(getApplicationContext(), "Tour", Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void getdata(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/adminpanel/cancel.php", response -> {

            try {



                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(cancelled_reports.this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);
                    getname=json.getString("supported_by");
                    getcount=json.getString("ecount");

                    adapter.setHname(getname);
                    adapter.setRcunt(getcount);

                    list.add(adapter);
                }

                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {

                Toast.makeText(this, "No Bookings Done Yet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                recyclerView.setVisibility(View.GONE);

            }


        }, error -> {

            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }){

            @Override
            protected Map <String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",FromDate);
                params.put("book_end_date",ToDate);
                params.put("category",category);
                Log.i("pa", String.valueOf(params));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);

    }

    private void getdata11(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/adminpanel/employee_rangewise.php", response -> {

            try {



                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(cancelled_reports.this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);
                    getname=json.getString("supported_by");
                    getcount=json.getString("ecount");

                    adapter.setHname(getname);
                    adapter.setRcunt(getcount);

                    list.add(adapter);
                }

                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {

                Toast.makeText(this, "No Bookings Done Yet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                recyclerView.setVisibility(View.GONE);

            }


        }, error -> {

            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",FromDate);
                params.put("book_end_date",ToDate);
                params.put("category",category);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);

    }


    public class hotel_recycle_adapter  extends RecyclerView.Adapter<cancelled_reports.hotel_recycle_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;

        hotel_recycle_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public cancelled_reports.hotel_recycle_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new cancelled_reports.hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employeenames, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull cancelled_reports.hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);


             Log.i("str",getname);


            if(!DDataAdapter.getHname().equals("")){
                vh.hnameTv.setText(DDataAdapter.getHname());
            }
            else{
                vh.hnameTv.setText("Tripnetra");
            }

            vh.rcountTV.setText(DDataAdapter.getRcunt());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder{

            TextView hnameTv,rcountTV,lines;
            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);
                lines = iv.findViewById(R.id.line);
                lines.setVisibility(View.GONE);
               /* iv.setOnClickListener(this);*/


            }

          /*  @Override
            public void onClick(View view) {
                getAdapname= listadapter.get(getAdapterPosition()).getRcunt();
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.cancelact);
                TextView txt=dialog.findViewById(R.id.count);
                txt.setText(getAdapname);
                Objects.requireNonNull(dialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
               // getdata11();
                dialog.show();
            }*/
        }
    }
}
