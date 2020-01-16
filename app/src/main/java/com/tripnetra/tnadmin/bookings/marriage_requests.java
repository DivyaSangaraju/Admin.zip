package com.tripnetra.tnadmin.bookings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import java.util.Objects;


    public class marriage_requests extends Fragment {

    Button CinBtn;
    String Url="https://tripnetra.com/androidphpfiles/adminpanel/marriage_requests1.php";
    CheckBox rechk,wilng,clsd,cnfrmd;
    RecyclerView recyclerView;
    String FromDate,Category;
    TextView goback;
    long mindate;int dateflag=0,ticket;
    CustomLoading cloading;


    public marriage_requests() { }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_marriage_requests, container, false);
        recyclerView =view.findViewById(R.id.marriagecycler);
        CinBtn = view.findViewById(R.id.cinBtn);
        rechk =view.findViewById(R.id.rechkCB);
        wilng=view.findViewById(R.id.wilngCB);
        clsd=view.findViewById(R.id.clsdCB);
        cnfrmd=view.findViewById(R.id.confCB);
        goback=view.findViewById(R.id.loadoldTV);
        cloading = new CustomLoading(getActivity());
        cloading.setCancelable(false);
        assert cloading.getWindow() != null;
        cloading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        long fdate = System.currentTimeMillis();
        FromDate = Utils.DatetoStr(fdate,0);
        CinBtn.setOnClickListener(v -> {
            mindate = System.currentTimeMillis() - 7776000000L;
            dateflag = 1;
            datedialog();
        });

        rechk.setOnClickListener(view1 -> {
            Category="RECHECK";
            ticket=1;
            wilng.setChecked(false);
            clsd.setChecked(false);
            cnfrmd.setChecked(false);
            marriageDetails();

        });

        wilng.setOnClickListener(view1 -> {
            Category="WILLING";
            ticket=1;
            clsd.setChecked(false);
            rechk.setChecked(false);
            cnfrmd.setChecked(false);
            marriageDetails();


        });

        clsd.setOnClickListener(view1 -> {
            Category="CLOSED";
            ticket=1;
            rechk.setChecked(false);
            wilng.setChecked(false);
            cnfrmd.setChecked(false);
            marriageDetails();

        });


        cnfrmd.setOnClickListener(view1 -> {
            Category="CONFIRMED";
            ticket=1;
            rechk.setChecked(false);
            wilng.setChecked(false);
            clsd.setChecked(false);
            marriageDetails();

        });



        goback.setOnClickListener(view12 -> {
            marriageDetails();
            ticket=2;
            CinBtn.clearFocus();
            CinBtn.setText("");
            clsd.setChecked(false);
            rechk.setChecked(false);
            wilng.setChecked(false);
            cnfrmd.setChecked(false);


        });

        marriageDetails();

        return view;
    }

    public void datedialog(){

        final Calendar cal = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), (view, year, month, day) -> {
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

    private void marriageDetails() {

        cloading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, response -> {
            cloading.dismiss();
            List<DataAdapter> list = new ArrayList<>();
            Log.i("res",response);

            recyclerView.setVisibility(View.VISIBLE);
            try {

                JSONArray jsonarray = new JSONArray(response);

                for(int i=0; i < jsonarray.length(); i++) {

                    DataAdapter dataAdapter = new DataAdapter();

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    dataAdapter.setHname(jobj.getString("added_by"));
                    dataAdapter.setHid(jobj.getString("added_date"));
                    dataAdapter.setGname(jobj.getString("request_description"));
                    dataAdapter.setMobile(jobj.getString("request_contact_number"));
                    dataAdapter.setRName(jobj.getString("request_place"));
                    dataAdapter.setType(jobj.getString("request_status"));
                    dataAdapter.setCarName(jobj.getString("response"));
                    dataAdapter.setRcunt(jobj.getString("request_details_id"));
                    dataAdapter.setCap2(jobj.getString("request_date"));
                    dataAdapter.setNewName(jobj.getString("followup_date"));

                    list.add(dataAdapter);


                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new hotels_adapter(list,getContext())); }

            catch (JSONException e) {

                recyclerView.setVisibility(View.GONE);
            }

        }, error -> {

            Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();

        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params =new HashMap<>();


                if(ticket==1){

                params.put("date",FromDate);
                params.put("category",Category);
                params.put("type","filter");}
                else{
                    params.put("type","All");
                }
                Log.i("params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        marriageDetails();
        super.onResume();
    }
}



class hotels_adapter extends RecyclerView.Adapter<hotels_adapter.ViewHolder> {

    private List<DataAdapter> listadapter;
    private Context con;
    private String detailsData;


    hotels_adapter(List<DataAdapter> listAdapter,Context c) {
        super();
        this.listadapter = listAdapter;
        this.con = c;
    }

    @NonNull
    @Override
    public hotels_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new hotels_adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_marriage, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView AdminnameTv,status,email,mobileno,desgn,dept,share,respns,flw;

        @SuppressLint("SetJavaScriptEnabled")
        ViewHolder(View iv) {

            super(iv);

            AdminnameTv = iv.findViewById(R.id.AdmnnameTV);
            status = iv.findViewById(R.id.AstatusTv);
           // email = iv.findViewById(R.id.AemailTV);
            mobileno = iv.findViewById(R.id.AmobileTv);
            desgn = iv.findViewById(R.id.DesignationTV);
            dept = iv.findViewById(R.id.DeptTV);
            share = iv.findViewById(R.id.shareTV);
            respns = iv.findViewById(R.id.addrespTV);
            flw = iv.findViewById(R.id.flwupTV);
            respns.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if(view==respns) {
                Intent intent =  new Intent(con, Marriage_Add_Rsp.class);
                intent.putExtra("id", listadapter.get(getAdapterPosition()).getRcunt());//ok
                intent.putExtra("status", listadapter.get(getAdapterPosition()).getType());//ok
                intent.putExtra("contact", listadapter.get(getAdapterPosition()).getMobile());//ok
                intent.putExtra("date", listadapter.get(getAdapterPosition()).getCap2());//ok
                con.startActivity(intent);

            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull hotels_adapter.ViewHolder vh, int pos) {

        DataAdapter dataAdap = listadapter.get(pos);
        vh.AdminnameTv.setText(dataAdap.getHname());
        vh.status.setText(dataAdap.getHid());
        //vh.email.setText(dataAdap.getGname());description
        vh.mobileno.setText(dataAdap.getMobile());//contact no
        vh.desgn.setText(dataAdap.getRName());//place
        vh.dept.setText(dataAdap.getType());
        vh.flw.setText(dataAdap.getNewName());
       // vh.descwebvw.loadData(dataAdap.getCarName(),"text/html", "UTF-8");

        vh.share.setOnClickListener(v -> {
            String result = Html.fromHtml(dataAdap.getCarName()).toString();
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            detailsData="Phone No :"+dataAdap.getMobile()+"\n"+"Marriage Date :"+dataAdap.getCap2()+"\n"+"Description :"+dataAdap.getGname()+"\n"+"Place :"+dataAdap.getRName()+"\n"+"Previous Conversation:"+"\n"+"\n"+result;
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

