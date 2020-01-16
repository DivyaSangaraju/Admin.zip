package com.tripnetra.tnadmin.Admin_Management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import java.util.List;
import java.util.Objects;

public class Admin_mgnt extends Fragment {

    RecyclerView recyclerView;
    Button addadmin;
    String Url="https://tripnetra.com/cpanel_admin/admin/admin_list/6865446727eae9cbd513";
    String data;
    FrameLayout flyt;

    public Admin_mgnt() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_admin_mgnt, container, false);
        recyclerView =view.findViewById(R.id.Adnrecyclerview);
        addadmin = view.findViewById(R.id.addadminBtn);
        flyt = view.findViewById(R.id.mainfly);
        addadmin.setOnClickListener(v -> {
         Intent intent = new Intent(getActivity(),Admin_list.class);
         startActivity(intent);
        });
        admindetails();
        return view;
    }

    private void admindetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            List<DataAdapter> list = new ArrayList<>();

            Log.i("res",response);

            recyclerView.setVisibility(View.VISIBLE);
            try {

                JSONArray jsonarray = new JSONObject(response).getJSONArray("admin_info");

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("admin_name"));
                    dataAdapter.setHid(jobj.getString("admin_status"));
                    dataAdapter.setGname(jobj.getString("admin_email"));
                    dataAdapter.setMobile(jobj.getString("admin_cell_phone"));
                    dataAdapter.setRName(jobj.getString("designation_name"));
                    dataAdapter.setType(jobj.getString("role_name"));

                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new hotels_adapter(list)); }

            catch (JSONException e) {

                recyclerView.setVisibility(View.GONE);
            }

        }, error -> {

             Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();

        });

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }


    class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

        private List<DataAdapter> listadapter;

        hotels_adapter(List<DataAdapter> listAdapter) {
            super();
            this.listadapter = listAdapter;
        }

        @NonNull
        @Override
        public hotels_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotels_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_adminlist, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView AdminnameTv,status,email,mobileno,desgn,dept;
            ViewHolder(View iv) {

                super(iv);

                AdminnameTv = iv.findViewById(R.id.AdmnnameTV);
                status = iv.findViewById(R.id.AstatusTv);
                email = iv.findViewById(R.id.AemailTV);
                mobileno = iv.findViewById(R.id.AmobileTv);
                desgn = iv.findViewById(R.id.DesignationTV);
                dept = iv.findViewById(R.id.DeptTV);

            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull hotels_adapter.ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);
            vh.AdminnameTv.setText(dataAdap.getHname());
            vh.status.setText(dataAdap.getHid());
            vh.email.setText(dataAdap.getGname());
            vh.mobileno.setText(dataAdap.getMobile());
            vh.desgn.setText(dataAdap.getRName());
            vh.dept.setText(dataAdap.getType());


        }

        @Override
        public int getItemCount() { return listadapter.size(); }
    }


}
