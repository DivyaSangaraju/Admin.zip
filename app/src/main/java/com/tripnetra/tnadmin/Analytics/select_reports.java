package com.tripnetra.tnadmin.Analytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tripnetra.tnadmin.Analytics_price.PriceCount;
import com.tripnetra.tnadmin.Analytics_price.comp_select;
import com.tripnetra.tnadmin.R;
import com.tripnetra.tnadmin.utils.Change_ip_address;
import com.tripnetra.tnadmin.utils.G46567;
import com.tripnetra.tnadmin.utils.Session;

import java.util.Objects;

public class select_reports extends Fragment {
    Activity activity;
    Button hotel,darshan,tour,prc,daily,employee,employeer,compare,cancelled;
    Session session;
    String LoginID,UserType,UserName,UserId;

    CardView htl,dr,tr,pr,dail,empldly,empmntly,comp,Cancellation;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report, container, false);

        //todo changed here

        activity=getActivity();

        htl=view.findViewById(R.id.ch);
        dr=view.findViewById(R.id.cd);
        tr=view.findViewById(R.id.ct);
        pr=view.findViewById(R.id.cp);
        dail=view.findViewById(R.id.cdl);
        empldly=view.findViewById(R.id.cem);
        empmntly=view.findViewById(R.id.cempm);
        comp=view.findViewById(R.id.ccomp);
        Cancellation=view.findViewById(R.id.cancelCV);


        session = new Session(Objects.requireNonNull(getActivity()));
        LoginID = session.getULogid();
        String Userid = session.getUId();
        UserType = session.getUType();
        UserName = session.getUName();
        UserId  = session.getUId();
        G46567 g665 = (G46567) getActivity().getApplicationContext();
        g665.setUserId(Userid);
        g665.setUserName(UserName);
        g665.setUserType(UserType);


        // price visbile to only superadmin 41 53
        if(UserId.equals("41") || UserId.equals("53")){
            pr.setVisibility(View.VISIBLE);
           // cancelled.setVisibility(View.VISIBLE);
        }


        htl.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),HotelCount.class);
            startActivity(i);
        });
        dr.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),Darshan_Count.class);
            startActivity(i);
        });
        tr.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),ToursCount.class);
            startActivity(i);
        });

        pr.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(), PriceCount.class);
           startActivity(i);
           // Toast.makeText(g665, "Access not available to view this", Toast.LENGTH_SHORT).show();
        });
        dail.setOnClickListener(view1 -> {

            Intent i=new Intent(getActivity(),Daily_bookings2.class);
            startActivity(i);
        });
        empldly.setOnClickListener(view1 -> {

            Intent i=new Intent(getActivity(),Employee_daywise.class);
            startActivity(i);
        });


        empmntly.setOnClickListener(view1 -> {

            Intent i=new Intent(getActivity(),Employee_rangewise.class);
            startActivity(i);
        });

        comp.setOnClickListener(view12 -> {
            Intent i=new Intent(getActivity(),comp_select.class);
            startActivity(i);
        });

        Cancellation.setOnClickListener(view12 -> {
            Intent i=new Intent(getActivity(), CancellationAct.class);
            startActivity(i);
        });

/*
        cancelled.setOnClickListener(view13 -> {
            startActivity(new Intent(getActivity(),Profits_activtiy.class));
        });*/




































        /*activity = getActivity();
        hotel=view.findViewById(R.id.hotel);
        darshan=view.findViewById(R.id.darshan);
        tour=view.findViewById(R.id.tour);
        prc=view.findViewById(R.id.prices);
        daily=view.findViewById(R.id.daily);
        employee=view.findViewById(R.id.employee);
        employeer=view.findViewById(R.id.employeerange);
        compare=view.findViewById(R.id.compreport);
        cancelled=view.findViewById(R.id.cancel_reports);
        session = new Session(Objects.requireNonNull(getActivity()));
        LoginID = session.getULogid();
        String Userid = session.getUId();
        UserType = session.getUType();
        UserName = session.getUName();
        UserId  = session.getUId();
        G46567 g665 = (G46567) getActivity().getApplicationContext();
        g665.setUserId(Userid);
        g665.setUserName(UserName);
        g665.setUserType(UserType);


        // price visbile to only superadmin
      if(UserId.equals("41") || UserId.equals("53")){
            prc.setVisibility(View.VISIBLE);
            cancelled.setVisibility(View.VISIBLE);
        }


        hotel.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),HotelCount.class);
            startActivity(i);
        });
        darshan.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),Darshan_Count.class);
            startActivity(i);
        });
        tour.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),ToursCount.class);
            startActivity(i);
        });

        prc.setOnClickListener(view1 -> {
            Intent i=new Intent(getActivity(),PriceCount.class);
            startActivity(i);
        });
        daily.setOnClickListener(view1 -> {

            Intent i=new Intent(getActivity(),Daily_bookings2.class);
            startActivity(i);
        });
        employee.setOnClickListener(view1 -> {

           Intent i=new Intent(getActivity(),Employee_daywise.class);
            startActivity(i);
        });


        employeer.setOnClickListener(view1 -> {

           Intent i=new Intent(getActivity(),Employee_rangewise.class);
            startActivity(i);
        });

        compare.setOnClickListener(view12 -> {
            Intent i=new Intent(getActivity(),comp_select.class);
            startActivity(i);
        });


        cancelled.setOnClickListener(view13 -> {
                startActivity(new Intent(getActivity(),Profits_activtiy.class));
        });*/
        return view;
    }
}
