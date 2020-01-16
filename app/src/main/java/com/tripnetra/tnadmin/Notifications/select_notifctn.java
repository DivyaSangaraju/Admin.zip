package com.tripnetra.tnadmin.Notifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tripnetra.tnadmin.R;


public class select_notifctn extends Fragment {
    Button btndrsn;


    public select_notifctn() { }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View vi=   inflater.inflate(R.layout.select_notifctn, container, false);
      btndrsn=vi.findViewById(R.id.btndrshn);
      btndrsn.setOnClickListener(view -> {
          Intent go=new Intent(getActivity(),Darshan_recycler_first.class);
          startActivity(go);
      });
      return vi;
    }




}
