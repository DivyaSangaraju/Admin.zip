package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import java.util.Objects;

public class Employee_daywise extends AppCompatActivity {

    Button CinBtn;
    CheckBox htl,tr,drshn;
    String FromDate,Category;
    long mindate;int dateflag=0;
    RecyclerView recyclerView;
    TextView t1,t2,t3,t11,t22,ho,hos;
    LinearLayout head,head1,lnrl,lnrs;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_daywise);
        CinBtn = findViewById(R.id.cinBtn);
        htl = findViewById(R.id.htl);
        tr = findViewById(R.id.tr);
        drshn = findViewById(R.id.drshn);
        lnrl=findViewById(R.id.lnr);
        lnrs=findViewById(R.id.lnrs);
        recyclerView=findViewById(R.id.Recycleemp);
        t1=findViewById(R.id.h1);
        t2=findViewById(R.id.h2);
        t3=findViewById(R.id.h3);
        t11=findViewById(R.id.h11);
        t22=findViewById(R.id.h22);
        head=findViewById(R.id.heading);
        head1=findViewById(R.id.heading1);
        ho=findViewById(R.id.ho);
        hos=findViewById(R.id.hos);
        recyclerView = findViewById(R.id.Recycleemp);
        long fdate = System.currentTimeMillis();
        FromDate = Utils.DatetoStr(fdate,0);
        CinBtn.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;
            dateflag = 1;
            datedialog();
        });

        htl.setOnClickListener(view -> {
            Category="hotel";
            lnrl.setVisibility(View.VISIBLE);
            lnrs.setVisibility(View.GONE);
            tr.setChecked(false);
            drshn.setChecked(false);
            getdata();
            hotelscount();
        });

        tr.setOnClickListener(view -> {
            Category="tour";
            lnrl.setVisibility(View.GONE);
            lnrs.setVisibility(View.GONE);
            htl.setChecked(false);
            drshn.setChecked(false);

            getdata();
        });

        drshn.setOnClickListener(view -> {
            Category="darshan";
            lnrl.setVisibility(View.VISIBLE);
            lnrs.setVisibility(View.VISIBLE);
            htl.setChecked(false);
            tr.setChecked(false);
            getdata();
            darshancount();
            packagecount();
        });
    }


    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Employee_daywise.this, (view, year, month, day) -> {
            Calendar calndr = Calendar.getInstance();
            calndr.set(year,month,day);

            if (dateflag == 1) {

                Calendar ncal = Calendar.getInstance();
                ncal.setTime(calndr.getTime());
                ncal.add(Calendar.DATE, 1);
                FromDate = Utils.DatetoStr(calndr.getTime(),0);
                CinBtn.setText(Utils.DatetoStr(calndr.getTime(),1));
                mindate = calndr.getTimeInMillis();
                dateflag = 2;


            }

        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();

    }
    private void hotelscount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/extranet/dailynew1.php" , response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("bcount1");
                    ho.setText("Hotels :"+name);
                    ho.setAnimation(AnimationUtils.loadAnimation(this,R.anim.flash_leave_now));

                    Log.i("name1", String.valueOf(name));


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","hotel");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Employee_daywise.this));
        requestQueue.add(stringRequest);
    }



    private void packagecount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/extranet/dailynew1.php", response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String name = jsonobject.getString("darcount1");
                    hos.setText("Packages:"+name);
                    hos.setAnimation(AnimationUtils.loadAnimation(this,R.anim.flash_leave_now));


                    Log.i("name1", String.valueOf(name));


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

            Toast.makeText(this, "Something went wrong Try Again.", Toast.LENGTH_LONG).show();
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","darshan");
                params.put("type","notsingle");


                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue((this));
        requestQueue.add(stringRequest);
    }



    private void darshancount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/extranet/dailynew1.php", response -> {


            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String name = jsonobject.getString("adcount");
                    ho.setText("Adults :"+name);
                    Log.i("name1", String.valueOf(name));


                }


            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {

            Toast.makeText(Employee_daywise.this, "Something went wrong Try Again.", Toast.LENGTH_LONG).show();
        })


        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                params.put("date",FromDate);
                params.put("category","darshancount");



                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Employee_daywise.this));
        requestQueue.add(stringRequest);
    }


    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/extranet/empnamebkngs.php", response -> {

            recyclerView.setVisibility(View.VISIBLE);

               List<DataAdapter> list = new ArrayList<>();
               try {

                   JSONArray jarr = new JSONArray(response);
                   for (int i = 0; i < jarr.length(); i++) {
                       DataAdapter dataAdapter = new DataAdapter();

                       JSONObject jobj = jarr.getJSONObject(i);



                       if(Category.equals("darshan")){

                       dataAdapter.setHname(jobj.getString("supported_by"));
                       dataAdapter.setRcunt("Packages :"+jobj.getString("bcount"));
                       dataAdapter.setStatus("Adults :"+jobj.getString("acount"));}

                       else {

                       dataAdapter.setHname(jobj.getString("supported_by"));
                       dataAdapter.setRcunt("Rooms :"+jobj.getString("bcount"));
                       }

                       list.add(dataAdapter);


                   }
                   recyclerView.setLayoutManager(new LinearLayoutManager(Employee_daywise.this));
                   recyclerView.setAdapter(new hotel_recycle_adapter(list));

               } catch (JSONException e) {
                   e.printStackTrace();
                   Toast.makeText(this, "No Bookings Done Yet", Toast.LENGTH_SHORT).show();
                   recyclerView.setVisibility(View.GONE);


               }

      }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("date",FromDate);
                params.put("category",Category);

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
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employeenames1, parent, false));
        }


        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);
            if(!DDataAdapter.getHname().equals("")){
                vh.hnameTv.setText(DDataAdapter.getHname());
            }
            else{
                vh.hnameTv.setText("Tripnetra");
                }

            vh.rcountTV.setText(DDataAdapter.getRcunt());

            if(DDataAdapter.getStatus()==null){
                vh.adultsTV.setText("");
            } else{
                vh.adultsTV.setText(DDataAdapter.getStatus());

            }

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView hnameTv,rcountTV,adultsTV;
            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                hnameTv = iv.findViewById(R.id.CNameTV);
                rcountTV = iv.findViewById(R.id.RoomCountTV);
                adultsTV = iv.findViewById(R.id.adultsTV);


            }
        }
    }

}
