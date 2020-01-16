package com.tripnetra.tnadmin.inventory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.CustomLoading;
import com.tripnetra.tnadmin.DashBoardActivity;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.adapters.DataAdapter;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tour_Invtry_Details_Act extends AppCompatActivity {
    String PName,PnameID,key;
    RecyclerView recyclerView;
    CustomLoading cloading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_invtry_details);

        recyclerView = findViewById(R.id.Tpackagerecycler);

        assert getIntent().getExtras() != null;
        PName = getIntent().getExtras().getString("hname");
        PnameID = getIntent().getExtras().getString("hnameid");
        ((TextView) findViewById(R.id.Tpacktv)).setText(PName);

      /*  cloading = new CustomLoading(this);
        cloading.setCancelable(false);
        assert cloading.getWindow()!=null;
        cloading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));*/

        getpackagedetails();

    }

    void getpackagedetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/tour_details.php", response -> {

            // Log.i("tour_resp",response);

            if (response.equals("No Result")) {
                Utils.setSingleBtnAlert(Tour_Invtry_Details_Act.this, "No packages Found", "Ok", true);
            } else {
                List<DataAdapter> list = new ArrayList<>();
                try {

                    JSONArray jarr = new JSONArray(response);
                    for (int i = 0; i < jarr.length(); i++) {
                        DataAdapter dataAdapter = new DataAdapter();

                        JSONObject jobj = jarr.getJSONObject(i);

                        dataAdapter.setHname(jobj.getString("car_name"));
                        dataAdapter.setOutDate(jobj.getString("car_price"));
                        dataAdapter.setHid(jobj.getString("car_id"));
                        dataAdapter.setStatus(jobj.getString("max_capacity"));
                        dataAdapter.setInDate(jobj.getString("actual_price"));

                        dataAdapter.setGname(PName);
                        dataAdapter.setType(PnameID);

                        list.add(dataAdapter);

                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(Tour_Invtry_Details_Act.this));
                    recyclerView.setAdapter(new Tour_Invtry_Details_Act.Recycler_Adapter(list));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Tour_Invtry_Details_Act.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Utils.setSingleBtnAlert(Tour_Invtry_Details_Act.this, "SomeThing Went Wromg\nPlease Try Again", "Ok", false);

                }
            }

        }, error -> {

            Utils.setSingleBtnAlert(Tour_Invtry_Details_Act.this, "SomeThing Went Wromg", "Ok", false);

        })


        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =new HashMap<>();
                params.put("type", "single");
                params.put("sightseen_id", PnameID);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(Tour_Invtry_Details_Act.this));
        requestQueue.add(stringRequest);

    }

    class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

        private List<DataAdapter> listadapter;

        Recycler_Adapter(List<DataAdapter> listAdapter) {
            super();
            this.listadapter = listAdapter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.car_search_recycler, parent, false));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView CarRNameTV,CarpriceTv,CaractTv,CarcapTv;
            Button ManageBtn;

            ViewHolder(View iv) {

                super(iv);

                CarRNameTV = iv.findViewById(R.id.CarRNameTV);
                CarpriceTv = iv.findViewById(R.id.CarpriceTv);
                CaractTv = iv.findViewById(R.id.CaractpTV);
                CarcapTv = iv.findViewById(R.id.CarcapTv);
                ManageBtn = iv.findViewById(R.id.managebtn);

                Context context = iv.getContext();

                ManageBtn.setOnClickListener(v -> {
                    Intent intent =  new Intent(context, Tour_Manage_Invtry_Act.class);
                    intent.putExtra("car_name", listadapter.get(getAdapterPosition()).getHname());
                    intent.putExtra("car_id", listadapter.get(getAdapterPosition()).getHid());
                    intent.putExtra("sightseen_name",listadapter.get(getAdapterPosition()).getGname());
                    intent.putExtra("sightseen_id",listadapter.get(getAdapterPosition()).getType());
                    intent.putExtra("max_capacity",listadapter.get(getAdapterPosition()).getStatus());
                    intent.putExtra("actual_price",listadapter.get(getAdapterPosition()).getInDate());
                    intent.putExtra("car_price", listadapter.get(getAdapterPosition()).getOutDate());

                    context.startActivity(intent);
                });

            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Tour_Invtry_Details_Act.Recycler_Adapter.ViewHolder vh, int pos) {

            DataAdapter dataAdap = listadapter.get(pos);

            String ss = "<p><span style=\"color: #011EBF;\">"+dataAdap.getHname()+"</span></p>";
            String sa = "<p><span style=\"color: #808080;\">Actual Price :</span> <span style=\"color: #000000;\">"+dataAdap.getInDate()+"</span></p>";
            String sb = "<p><span style=\"color: #808080;\">Partial Price :</span> <span style=\"color: #000000;\">"+dataAdap.getOutDate()+"</span></p>";
            String sc = "<p><span style=\"color: #808080;\">Capacity :</span> <span style=\"color: #000000;\">"+dataAdap.getStatus()+"</span></p>";

            vh.CarRNameTV.setText(Html.fromHtml(ss));




            vh.CarpriceTv.setText(Html.fromHtml(sb));
            vh.CaractTv.setText(Html.fromHtml(sa));
            vh.CarcapTv.setText(Html.fromHtml(sc));

        }

        @Override
        public int getItemCount() { return listadapter.size(); }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
