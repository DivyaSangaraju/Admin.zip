package com.tripnetra.tnadmin.utils;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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

import static android.view.View.VISIBLE;

public class Change_ip_address extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout addNlnr;
    Button subbtn;
    EditText addadrsEt,addnameEt;
    String newAddrs,newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip_address);
        recyclerView=findViewById(R.id.cycle);
        addNlnr=findViewById(R.id.addNew);
        subbtn=findViewById(R.id.subBtn);
        addadrsEt=findViewById(R.id.addNameET);
        addnameEt=findViewById(R.id.addAddrsET);
        getIpaddress();
    }

    private void getIpaddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/test/adminpanel/iplist.php", response -> {

            recyclerView.setVisibility(VISIBLE);

            List<DataAdapter> list = new ArrayList<>();
            try {

                JSONArray jarr = new JSONArray(response);
                for (int i = 0; i < jarr.length(); i++) {
                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jarr.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("ip_id"));
                    dataAdapter.setGname(jobj.getString("ip_name"));
                    dataAdapter.setRName(jobj.getString("ip_address"));
                    list.add(dataAdapter);

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(Change_ip_address.this));
                recyclerView.setAdapter(new employee_names_adapter(list));

            } catch (JSONException e) {
                e.printStackTrace();


            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        requestQueue.add(stringRequest);
    }

    public void addnew(View view) {
        addNlnr.setVisibility(VISIBLE);
        subbtn.setVisibility(VISIBLE);
    }

    public void insertIp(View view) {

        newAddrs=addadrsEt.getText().toString().trim();
        newName=addnameEt.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/test/adminpanel/insip.php", response -> {
            Toast.makeText(this, response+"Added Successfully", Toast.LENGTH_SHORT).show();

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();

                params.put("ip_address",newAddrs);
                params.put("ip_name",newName);

                Log.i("c", String.valueOf(params));
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public class employee_names_adapter  extends RecyclerView.Adapter<employee_names_adapter.ViewHolder>  {

        private Context context;
        private List<DataAdapter> listadapter;
        String sId,sName, sAdrs;


        employee_names_adapter(List<DataAdapter> list) {
            super();
            this.listadapter = list;
        }

        @NonNull
        @Override
        public employee_names_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new employee_names_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull employee_names_adapter.ViewHolder vh, int pos) {

            DataAdapter DDataAdapter =  listadapter.get(pos);

            vh.id.setText(DDataAdapter.getHname());
            vh.ipname.setText(DDataAdapter.getGname());
            vh.ipadrs.setText(DDataAdapter.getRName());

        }

        @Override
        public int getItemCount() { return listadapter.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView edit,save;
            EditText id,ipname,ipadrs;
            String upIpadrs,upIpname;


            ViewHolder(View iv) {
                super(iv);

                context = iv.getContext();
                id = iv.findViewById(R.id.Tvid);
                ipname = iv.findViewById(R.id.Tvipnm);
                ipadrs = iv.findViewById(R.id.Tvipadrs);
                id.setEnabled(false);
                ipname.setEnabled(false);
                ipadrs.setEnabled(false);
                edit =iv.findViewById(R.id.btnEdit);
                save =iv.findViewById(R.id.btnSave);

                edit.setOnClickListener(view -> {
                    sId =listadapter.get(getAdapterPosition()).getHname();
                    sName=listadapter.get(getAdapterPosition()).getGname();
                    sAdrs =listadapter.get(getAdapterPosition()).getRName();
                    Toast.makeText(context, "Edit"+"\n"+ sId +"\n"+sName+"\n"+ sAdrs, Toast.LENGTH_SHORT).show();
                    ipname.setEnabled(true);
                    ipadrs.setEnabled(true);

                });

                save.setOnClickListener(view -> {

                    upIpadrs=ipadrs.getText().toString();
                    upIpname=ipname.getText().toString();

                    sId =listadapter.get(getAdapterPosition()).getHname();
                    sName=listadapter.get(getAdapterPosition()).getGname();
                    sAdrs =listadapter.get(getAdapterPosition()).getRName();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/androidphpfiles/test/adminpanel/ipup.php", response -> {
                        Toast.makeText(context, response+"Updated Successfully", Toast.LENGTH_SHORT).show();

                    }, error -> {
                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    }){

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params =new HashMap<>();

                            params.put("ip_id",sId);
                            params.put("ip_name",upIpname);
                            params.put("ip_address", upIpadrs);
                            Log.i("p2", String.valueOf(params));
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                });

            }
        }
    }
}
