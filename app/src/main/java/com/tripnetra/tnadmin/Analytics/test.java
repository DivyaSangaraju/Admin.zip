package com.tripnetra.tnadmin.Analytics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import java.util.Objects;

public class test extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView=findViewById(R.id.Re);
        hotelscount();
    }

    private void hotelscount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/extranet/btest.php" , response -> {

            recyclerView.setVisibility(View.VISIBLE);

            List<DataAdapter> list = new ArrayList<>();
            Log.i("res",response);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jsonobject.getString("contact_fname"));
                    dataAdapter.setRcunt(jsonobject.getString("booking_status"));

                    list.add(dataAdapter);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(test.this));
                recyclerView.setAdapter(new hotel_recycle_adapter(list));


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, error -> {

        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("uid","109");
                params.put("Sdate","2019-07-01");
                params.put("Edate","2019-07-18");

                Log.i("params", String.valueOf(params));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(test.this));
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
            return new hotel_recycle_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employeenames1, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotel_recycle_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

                vh.hnameTv.setText(DDataAdapter.getHname());

            vh.rcountTV.setText(DDataAdapter.getRcunt());



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



