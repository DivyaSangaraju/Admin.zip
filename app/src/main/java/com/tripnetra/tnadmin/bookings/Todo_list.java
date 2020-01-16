package com.tripnetra.tnadmin.bookings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
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


public class Todo_list extends Fragment {

    String Click_Type="mrg_followup";
    RecyclerView recyclerView;

    public Todo_list() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_todo_list, container, false);
        recyclerView=v.findViewById(R.id.todolistRecyl);
        Todo_List();
        return v;
    }


    public void Todo_List() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://tripnetra.com/cpanel_admin/followup/followup_requests/"+Click_Type+"/6865446727eae9cbd513", response -> {
            List<DataAdapter> list = new ArrayList<>();
          //  Log.i("res",response);
           recyclerView.setVisibility(View.VISIBLE);

            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("added_by"));//added_name
                    dataAdapter.setHid(jobj.getString("added_date"));//added_date
                    dataAdapter.setGname(jobj.getString("request_description"));//description
                    dataAdapter.setMobile(jobj.getString("request_contact_number"));//client_contact_no
                    dataAdapter.setRName(jobj.getString("request_place"));//place_of_marriage
                    dataAdapter.setType(jobj.getString("request_status"));//status
                    dataAdapter.setCarName(jobj.getString("response"));//response
                    dataAdapter.setRcunt(jobj.getString("request_details_id"));//req_id
                    dataAdapter.setCap2(jobj.getString("request_date"));//marriage_date
                    dataAdapter.setNewName(jobj.getString("followup_date"));//followupd_date

                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new hotels_adapter(list,getContext())); }
            catch (JSONException e) {

                recyclerView.setVisibility(View.GONE);

            }
          //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();


        }, error -> {
            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();
                params.put("mrg_followup",Click_Type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }


    @Override
    public void onResume() {
        Todo_List();
        super.onResume();
    }

    class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

        private List<DataAdapter> listadapter;
        private Context con;
        String detailsData;


        hotels_adapter(List<DataAdapter> listAdapter,Context c) {
            super();
            this.listadapter = listAdapter;
            this.con = c;
        }

        @NonNull
        @Override
        public hotels_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new hotels_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_marriage, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView AdminnameTv,status,email,mobileno,desgn,dept,share,flw;
            WebView descwebvw;

            @SuppressLint("SetJavaScriptEnabled")
            ViewHolder(View iv) {

                super(iv);

                AdminnameTv = iv.findViewById(R.id.AdmnnameTV);
                status = iv.findViewById(R.id.AstatusTv);
                email = iv.findViewById(R.id.AemailTV);//description
                mobileno = iv.findViewById(R.id.mobilTV);//client cntct no
                desgn = iv.findViewById(R.id.DesignationTV);
                dept = iv.findViewById(R.id.DeptTV);
                share = iv.findViewById(R.id.shareTV);
                flw = iv.findViewById(R.id.flwTV);
                descwebvw=iv.findViewById(R.id.descWebvw);
                WebSettings webSettings = descwebvw.getSettings();
                webSettings.setJavaScriptEnabled(true);
                iv.setOnClickListener(this);


            }



            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(con, Todo_list_details.class);
                intent.putExtra("id", listadapter.get(getAdapterPosition()).getRcunt());//ok
                intent.putExtra("status", listadapter.get(getAdapterPosition()).getType());//ok
                intent.putExtra("contact", listadapter.get(getAdapterPosition()).getMobile());//ok
                intent.putExtra("date", listadapter.get(getAdapterPosition()).getCap2()) ;
                con.startActivity(intent); }//ok

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
            vh.flw.setText("Followup Date:"+" "+dataAdap.getNewName());

            vh.descwebvw.loadData(dataAdap.getCarName(),"text/html", "UTF-8");

            vh.share.setOnClickListener(v -> {

                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String result = Html.fromHtml(dataAdap.getCarName()).toString();
                detailsData=dataAdap.getGname()+"\n"+dataAdap.getMobile()+"\n"+dataAdap.getRName()+"Previous Conversation :"+"\n"+"\n"+result;
                ClipData clip = ClipData.newPlainText("Copied", detailsData);
                clipboard.setPrimaryClip(clip);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,detailsData );
                v.getContext().startActivity(Intent.createChooser(share, "Share Marriage Details to"));
            });


            vh.mobileno.setOnClickListener(v ->
                    new AlertDialog.Builder(con)
                            .setMessage("Do you want to call")
                            .setPositiveButton("Yes", (dialog, id) -> {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+dataAdap.getMobile()));
                                con.startActivity(intent);
                            })
                            .setNegativeButton("No", (dialog, id) -> { })
                            .setCancelable(true).create().show());

            String ss = dataAdap.getType();
            if(ss.contains("RECHECK")){
                vh.dept.setTextColor(Color.parseColor("#0127FC"));
            }else if(ss.contains("WILLING")){
                vh.dept.setTextColor(Color.parseColor("#B9B90D"));
            }else if(ss.contains("CLOSED")){
                vh.dept.setTextColor(Color.parseColor("#F50A0A"));
            }else{
                vh.dept.setTextColor(Color.parseColor("#157044"));
            }

        }
        @Override
        public int getItemCount() { return listadapter.size(); }
    }

}
