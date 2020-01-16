package com.tripnetra.tnadmin.inventory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Tour_Main_Invtry_Act extends Fragment implements View.OnClickListener {

    AutoCompleteTextView TnameTv;
    Button SearchBtn;
    String TName, TNameId,SName,SId,SName1,SId1,CID;
    List<String> namesList, idsList,pkglist,idsList1,ilsit,catlist;
    Activity activity;

    Spinner packspi;
   // AutoCompleteTextView psearchtv;

    public Tour_Main_Invtry_Act() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tour_main_invtry, container, false);

        activity = getActivity();

        TnameTv = view.findViewById(R.id.TautocompTv);
        SearchBtn = view.findViewById(R.id.Tsearch_button);
        packspi = view.findViewById(R.id.packspi);
     //   psearchtv = view.findViewById(R.id.psearchtv);
        TnameTv.setHint("Package Name");

        SearchBtn.setOnClickListener(this);

        gethnamesdet();
        return view;
    }



    private void gethnamesdet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/tour_details.php", response -> {


           // Log.i("tour_resp",response);
            try {

                JSONArray jarr = new JSONArray(response);
                namesList = new ArrayList<>();
                idsList = new ArrayList<>();

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = jarr.getJSONObject(i);
                    namesList.add(json.getString("city_name"));
                    idsList.add(json.getString("city_details_id"));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(activity, R.layout.textview_layout, namesList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                TnameTv.setThreshold(1);
                TnameTv.setAdapter(dataAdapter);

                TnameTv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                    TName = String.valueOf(arg0.getItemAtPosition(arg2));
                    int ii = namesList.indexOf(TName);
                    TNameId = idsList.get(ii);
                    Log.i("TNameId",TNameId);
                    Log.i("TName",TName);



                    getspinnerdata();
                    TnameTv.clearFocus();
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                Utils.setSingleBtnAlert(activity, "SomeThing Went Wromg\nPlease Try Again", "Ok", false);

            }

        }, error -> {

            Utils.setSingleBtnAlert(activity, "SomeThing Went Wromg", "Ok", false);

        })


        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =new HashMap<>();
                params.put("type", "notsingle");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        requestQueue.add(stringRequest);
    }

    private void getspinnerdata(){

           // Toast.makeText(activity, "pressed on getspinnerdata", Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tripnetra.com/androidphpfiles/adminpanel/tour_details.php", response -> {


                 Log.i("tour_resp",response);
                try {

                    JSONArray jarr = new JSONArray(response);

                    pkglist = new ArrayList<>();
                    idsList1 = new ArrayList<>();
                    ilsit = new ArrayList<>();
                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject json = jarr.getJSONObject(i);
                        pkglist.add(json.getString("sightseen_name"));
                        ilsit.add(json.getString("sightseen_id"));

                        idsList1.add(TNameId);

                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.textview_layout, R.id.txt, pkglist);
                    packspi.setAdapter(dataAdapter);

                    Log.i("dataadp", String.valueOf(dataAdapter));
                    packspi.setPrompt("Select package");
                    packspi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View view, int position, long row_id) {
                            SName1 = String.valueOf(arg0.getItemAtPosition(position));
                            int iii = pkglist.indexOf(SName1);
                            SId1 = ilsit.get(iii);
                            Log.i("SId1",SId1);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                    Utils.setSingleBtnAlert(activity, "SomeThing Went Wromg\nPlease Try Again", "Ok", false);

                }

            }, error -> {

                Utils.setSingleBtnAlert(activity, "SomeThing Went Wromg", "Ok", false);

            })


            {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params =new HashMap<>();
                    params.put("type", "all");
                    params.put("city_details_id", TNameId);
                    Log.i("paramss", String.valueOf(params));

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            requestQueue.add(stringRequest);
                
        }


    @Override
    public void onClick(View v) {
        String concat = TName+" - "+ SName1;
        Log.i("concat",concat);

        Intent intent = new Intent(activity, Tour_Invtry_Details_Act.class);
        intent.putExtra("hname", concat);
        intent.putExtra("hnameid", SId1);
        //intent.putExtra("pname", );
        //  intent.putExtra("key", "0");

        startActivity(intent);
    }
}
