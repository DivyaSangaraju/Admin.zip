package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Emp_rangewise_HNames extends AppCompatActivity {

    String SDate,EDate,cat,sup;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_rangewise_hnames);


        recyclerView = findViewById(R.id.HnameRcylr);

        Bundle b = getIntent().getExtras();
        assert b!=null;
        SDate = b.getString("book_date");
        EDate = b.getString("book_end_date");
        cat = b.getString("category");
        sup = b.getString("suprt_by");

        Log.i("bdata",SDate +" "+ EDate +" "+ cat +" "+ sup);
        getdata();
    }
    private void getdata() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/prasanth/androidphpfiles/adminpanel/Emp_Mwise_Hnames.php", response -> {

            Log.i("empres",response);
            try {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                JSONArray jarr = new JSONArray(response);
                List<DataAdapter> list = new ArrayList<>();

                for(int i = 0; i< jarr.length(); i++) {
                    DataAdapter adapter = new DataAdapter();
                    JSONObject json = jarr.getJSONObject(i);

                    adapter.setHname(json.getString("name"));
                    adapter.setRcunt(json.getString("ecount"));

                    // supt_by = json.getString("supported_by");

                    list.add(adapter);
                }

                recyclerView.setAdapter(new hotel_recycle_adapter(list));

            } catch (JSONException e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                recyclerView.setVisibility(View.GONE);

            }

        }, error -> {

            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("book_date",SDate);
                params.put("book_end_date",EDate);
                params.put("category",cat);
                params.put("sup_by",sup);
                Log.i("emppa", String.valueOf(params));

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
            }
        }
    }

}
