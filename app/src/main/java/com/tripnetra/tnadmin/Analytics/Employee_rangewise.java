package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

public class Employee_rangewise extends AppCompatActivity {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button CinBtn,CoutBtn;
    CheckBox htl,tr,drsh;
    String FromDate,ToDate,Category;
    long mindate;int dateflag=0;
    RecyclerView recyclerView;
    LinearLayout head,head1;
    TextView t1,t2,t3,t11,t22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_rangewise);

        CinBtn = findViewById(R.id.cinBtn);
        CoutBtn = findViewById(R.id.coutBtn);
        htl = findViewById(R.id.b1);
        tr = findViewById(R.id.b2);
        drsh = findViewById(R.id.b3);
        recyclerView = findViewById(R.id.Recycleemp1);
        t1=findViewById(R.id.h1);
        t2=findViewById(R.id.h2);
        t3=findViewById(R.id.h3);
        t11=findViewById(R.id.h11);
        t22=findViewById(R.id.h22);
        head=findViewById(R.id.heading);
        head1=findViewById(R.id.heading1);

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

        htl.setOnClickListener(view -> {
            Category="hotel";
            t11.setText("Employee Name");
            t22.setText("Rooms");
            head1.setVisibility(View.VISIBLE);
            head.setVisibility(View.GONE);
            tr.setChecked(false);
            drsh.setChecked(false);
            getdata();

        });

        tr.setOnClickListener(view -> {
            Category="tour";
            t11.setText("Employee Name");
            t22.setText("Packages");
            head1.setVisibility(View.VISIBLE);
            head.setVisibility(View.GONE);
            drsh.setChecked(false);
            htl.setChecked(false);

            getdata();
        });

        drsh.setOnClickListener(view -> {
            Category="darshan";
            t1.setText("Employee Name");
            t2.setText("Packages");
            tr.setChecked(false);
            htl.setChecked(false);
            head.setVisibility(View.VISIBLE);
            head1.setVisibility(View.GONE);

            getdata();
        });


    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Employee_rangewise.this, (view, year, month, day) -> {
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

    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/prasanth/androidphpfiles/adminpanel/employee_rangewise.php", response -> {

            try {


                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(Employee_rangewise.this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setHname(json.getString("supported_by"));
                    adapter.setRcunt(json.getString("ecount"));

                   // supt_by = json.getString("supported_by");

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
                params.put("category",Category);
                Log.i("pa", String.valueOf(params));

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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employeenames, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.supt_by = DDataAdapter.getHname();

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

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,rcountTV;
            String supt_by;
            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        Intent i = new Intent(context,Emp_rangewise_HNames.class);

                        bundle.putString("book_date",FromDate);
                        bundle.putString("book_end_date",ToDate);
                        bundle.putString("category",Category);
                        bundle.putString("suprt_by",supt_by);
                        i.putExtras(bundle);

                        startActivity(i);
                    }
                });
            }
        }
    }

}








